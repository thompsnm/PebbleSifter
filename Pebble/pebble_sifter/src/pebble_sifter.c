#include <pebble.h>

#define max_sifters 10
#define max_sifter_name 32
#define max_sifter_full_name 64

const int vert_scroll_text_padding = 4;
const int header_display_height = 16;
const int sifter_name_layer_vert_size = 20;
int num_sifters = 0;
char sifter_names[max_sifters][max_sifter_name];
char sifter_full_names[max_sifters][max_sifter_full_name];

static struct MainScreenData {
  Window *window;
  ScrollLayer *sifter_text_scroll_layer;
  TextLayer *sifter_name_layer;
  TextLayer *sifter_text_layer;
  ScrollLayerCallbacks scroll_layer_callbacks;
  AppSync sync;
  uint8_t sync_buffer[1024];
} main_screen_data;

static struct SifterMenuData {
  Window *window;
  SimpleMenuLayer *simple_menu_layer;
  SimpleMenuSection menu_sections[1];
  SimpleMenuItem menu_items[max_sifters];
} sifter_menu_data;

enum {
  SIFTER_PEBBLE_NAME_KEY = 0x0,
  SIFTER_TEXT_KEY = 0x1,
  SIFTER_FULL_NAME_KEY = 0x2,
  SIFTER_PEBBLE_MENU_NAME_KEY = 0x3,
  HANDSHAKE_INIT_KEY = 0x4,
  HANDSHAKE_SUCCESS_KEY = 0x5,
  HANDSHAKE_FAIL_KEY = 0x6,
};

char *translate_app_message_error(AppMessageResult result) {
  switch (result) {
    case APP_MSG_OK: return "APP_MSG_OK";
    case APP_MSG_SEND_TIMEOUT: return "APP_MSG_SEND_TIMEOUT";
    case APP_MSG_SEND_REJECTED: return "APP_MSG_SEND_REJECTED";
    case APP_MSG_NOT_CONNECTED: return "APP_MSG_NOT_CONNECTED";
    case APP_MSG_APP_NOT_RUNNING: return "APP_MSG_APP_NOT_RUNNING";
    case APP_MSG_INVALID_ARGS: return "APP_MSG_INVALID_ARGS";
    case APP_MSG_BUSY: return "APP_MSG_BUSY";
    case APP_MSG_BUFFER_OVERFLOW: return "APP_MSG_BUFFER_OVERFLOW";
    case APP_MSG_ALREADY_RELEASED: return "APP_MSG_ALREADY_RELEASED";
    case APP_MSG_CALLBACK_ALREADY_REGISTERED: return "APP_MSG_CALLBACK_ALREADY_REGISTERED";
    case APP_MSG_CALLBACK_NOT_REGISTERED: return "APP_MSG_CALLBACK_NOT_REGISTERED";
    case APP_MSG_OUT_OF_MEMORY: return "APP_MSG_OUT_OF_MEMORY";
    case APP_MSG_CLOSED: return "APP_MSG_CLOSED";
    case APP_MSG_INTERNAL_ERROR: return "APP_MSG_INTERNAL_ERROR";
    default: return "UNKNOWN ERROR";
  }
}

char *translate_dictionary_error(DictionaryResult result) {
  switch (result) {
    case DICT_OK: return "DICT_OK";
    case DICT_NOT_ENOUGH_STORAGE: return "DICT_NOT_ENOUGH_STORAGE";
    case DICT_INVALID_ARGS: return "DICT_INVALID_ARGS";
    case DICT_INTERNAL_INCONSISTENCY: return "DICT_INTERNAL_INCONSISTENCY";
    case DICT_MALLOC_FAILED: return "DICT_MALLOC_FAILED";
    default: return "UNKNOWN ERROR";
  }
}

static void sync_error_callback(DictionaryResult dict_error, AppMessageResult app_message_error, void *context) {
    app_log(APP_LOG_LEVEL_ERROR, __FILE__, __LINE__, "Error in AppSync:");
    app_log(APP_LOG_LEVEL_ERROR, __FILE__, __LINE__, "AppMessageResult: %s", translate_app_message_error(app_message_error));
    app_log(APP_LOG_LEVEL_ERROR, __FILE__, __LINE__, "DictionaryResult: %s", translate_dictionary_error(dict_error));
}

static void sync_tuple_changed_callback(const uint32_t key, const Tuple* new_tuple, const Tuple* old_tuple, void* context) {
  switch (key) {
    case SIFTER_PEBBLE_NAME_KEY:
      text_layer_set_text(main_screen_data.sifter_name_layer, new_tuple->value->cstring);
      break;
    case SIFTER_TEXT_KEY:
      text_layer_set_text(main_screen_data.sifter_text_layer, new_tuple->value->cstring);
      GSize max_size = text_layer_get_content_size(main_screen_data.sifter_text_layer);
      text_layer_set_size(main_screen_data.sifter_text_layer, max_size);
      scroll_layer_set_content_size(main_screen_data.sifter_text_scroll_layer, GSize(144, max_size.h + vert_scroll_text_padding));
      break;
    default:
      return;
  }
}

static void send_sifter_select_cmd(const char sifter_select[]) {
  Tuplet value = TupletCString(SIFTER_FULL_NAME_KEY, sifter_select);
  
  DictionaryIterator *iter;
  app_message_outbox_begin(&iter);
  
  if (iter == NULL)
    return;
  
  dict_write_tuplet(iter, &value);
  dict_write_end(iter);
  
  app_message_outbox_send();
}

static void send_handshake_cmd() {
  Tuplet value = TupletInteger(HANDSHAKE_INIT_KEY, 1);

  DictionaryIterator *iter;
  app_message_outbox_begin(&iter);

  if (iter == NULL)
    return;

  dict_write_tuplet(iter, &value);
  dict_write_end(iter);

  app_message_outbox_send();
}

static void handle_deinit(void) {
  app_sync_deinit(&main_screen_data.sync);
  window_destroy(main_screen_data.window);
  scroll_layer_destroy(main_screen_data.sifter_text_scroll_layer);
  text_layer_destroy(main_screen_data.sifter_name_layer);
  text_layer_destroy(main_screen_data.sifter_text_layer);
  if (sifter_menu_data.window) {
    window_destroy(sifter_menu_data.window);
    simple_menu_layer_destroy(sifter_menu_data.simple_menu_layer);
  }
}

void menu_select_callback(int index, void *ctx) {
  window_stack_pop(sifter_menu_data.window);
  send_sifter_select_cmd(sifter_full_names[index]);
}

void sifter_menu_init() {
  app_log(APP_LOG_LEVEL_DEBUG, __FILE__, __LINE__, "Calling sifter_menu_init");

  // Initialize the menu window
  sifter_menu_data.window = window_create();
  window_stack_push(sifter_menu_data.window, true /* Animated */ );

  // Set up the menu items
  for(int i = 0; i < num_sifters; i++) {
    sifter_menu_data.menu_items[i] = (SimpleMenuItem){
      .title = sifter_names[i],
      .callback = menu_select_callback,
    };
  }

  // Bind the menu items to the corresponding menu sections
  sifter_menu_data.menu_sections[0] = (SimpleMenuSection){
    .num_items = num_sifters,
    .items = sifter_menu_data.menu_items,
  };

  GRect bounds = layer_get_bounds(window_get_root_layer(sifter_menu_data.window));

  // Initialize the simple menu layer
  sifter_menu_data.simple_menu_layer = simple_menu_layer_create(bounds, sifter_menu_data.window, sifter_menu_data.menu_sections, 1, NULL);

  // Add it to the window for display
  layer_add_child(window_get_root_layer(sifter_menu_data.window), simple_menu_layer_get_layer(sifter_menu_data.simple_menu_layer));
}

void select_single_click_handler(ClickRecognizerRef recognizer, Window *window) {
  sifter_menu_init();
}

void click_config_provider(Window *window) {
  window_single_click_subscribe(BUTTON_ID_SELECT, (ClickHandler) select_single_click_handler);
}

static void receive_handshake_cmd(DictionaryIterator *iter, void *context) {
  app_log(APP_LOG_LEVEL_DEBUG, __FILE__, __LINE__, "Calling receive_handshake_cmd");

  Tuple *menu_name_tuple = dict_find(iter, SIFTER_PEBBLE_MENU_NAME_KEY);
  if (menu_name_tuple) {
    app_log(APP_LOG_LEVEL_DEBUG, __FILE__, __LINE__, "Found SIFTER_PEBBLE_MENU_NAME_KEY with value %s and length %d", menu_name_tuple->value->cstring, strlen(menu_name_tuple->value->cstring));
    strncpy(sifter_names[num_sifters], menu_name_tuple->value->cstring, strlen(menu_name_tuple->value->cstring));
  }

  Tuple *full_name_tuple = dict_find(iter, SIFTER_FULL_NAME_KEY);
  if (full_name_tuple) {
    app_log(APP_LOG_LEVEL_DEBUG, __FILE__, __LINE__, "Found SIFTER_FULL_NAME_KEY with value %s and length %d", full_name_tuple->value->cstring, strlen(full_name_tuple->value->cstring));
    strncpy(sifter_full_names[num_sifters], full_name_tuple->value->cstring, strlen(full_name_tuple->value->cstring));
    num_sifters++;
  }

  Tuple *handshake_stop_tuple = dict_find(iter, HANDSHAKE_SUCCESS_KEY);
  if (handshake_stop_tuple) {
    app_log(APP_LOG_LEVEL_DEBUG, __FILE__, __LINE__, "Found HANDSHAKE_SUCCESS_KEY");

    // De-register message handler
    app_message_register_inbox_received(NULL);

    // Register select click handler
    main_screen_data.scroll_layer_callbacks.click_config_provider = (ClickConfigProvider) click_config_provider;
    scroll_layer_set_callbacks(main_screen_data.sifter_text_scroll_layer, main_screen_data.scroll_layer_callbacks);
    scroll_layer_set_click_config_onto_window(main_screen_data.sifter_text_scroll_layer, main_screen_data.window);

    // Update message displayed on screen
    text_layer_set_text(main_screen_data.sifter_name_layer, "Ready!");
    text_layer_set_text(main_screen_data.sifter_text_layer, "Please wait while the sifter list is populated.");

    // Set initial values for app sync
    Tuplet initial_values[] = {
      TupletCString(SIFTER_PEBBLE_NAME_KEY, "Ready!"),
      TupletCString(SIFTER_TEXT_KEY, "Please select a sifter from the menu."),
    };

    // Initialize app sync
    app_sync_init(&main_screen_data.sync, main_screen_data.sync_buffer, sizeof(main_screen_data.sync_buffer), initial_values, ARRAY_LENGTH(initial_values), sync_tuple_changed_callback, sync_error_callback, NULL);
  }
}

void main_screen_handle_init(void) {
  const GRect max_text_bounds = GRect(0, 0, 144, 2000);

  // Open AppMessage to transfers
  app_message_open(app_message_inbox_size_maximum(), app_message_outbox_size_maximum());

  // Initialize the main screen window
  main_screen_data.window = window_create();
  window_stack_push(main_screen_data.window, true /* Animated */ );

  // Initialize the sifter name layer and add it to the window
  main_screen_data.sifter_name_layer = text_layer_create(GRect(0, 0, 144, sifter_name_layer_vert_size));
  text_layer_set_text_alignment(main_screen_data.sifter_name_layer, GTextAlignmentCenter);
  text_layer_set_text(main_screen_data.sifter_name_layer, "Working...");
  layer_add_child(window_get_root_layer(main_screen_data.window), text_layer_get_layer(main_screen_data.sifter_name_layer));

  // Initialize the scroll layer
  main_screen_data.sifter_text_scroll_layer = scroll_layer_create(GRect(0, sifter_name_layer_vert_size, 144, (168 - sifter_name_layer_vert_size - header_display_height)));
  scroll_layer_set_content_size(main_screen_data.sifter_text_scroll_layer, max_text_bounds.size);

  // Initialize the sifter text layer
  main_screen_data.sifter_text_layer = text_layer_create(max_text_bounds);
  text_layer_set_text(main_screen_data.sifter_text_layer, "Please wait while the sifter list is populated.");

  // Trim text layer and scroll content to fit text box
  GSize max_size = text_layer_get_content_size(main_screen_data.sifter_text_layer);
  text_layer_set_size(main_screen_data.sifter_text_layer, max_size);
  scroll_layer_set_content_size(main_screen_data.sifter_text_scroll_layer, GSize(144, max_size.h + vert_scroll_text_padding));

  // Add the sifter text layer and scroll layer to the window
  scroll_layer_add_child(main_screen_data.sifter_text_scroll_layer, text_layer_get_layer(main_screen_data.sifter_text_layer));
  layer_add_child(window_get_root_layer(main_screen_data.window), scroll_layer_get_layer(main_screen_data.sifter_text_scroll_layer));

  // Register message handler
  app_message_register_inbox_received((AppMessageInboxReceived) receive_handshake_cmd);

  // Initialize handshake
  send_handshake_cmd();
}

int main(void) {
  main_screen_handle_init();
  app_event_loop();
  handle_deinit();
}

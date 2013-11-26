#include "pebble_os.h"
#include "pebble_app.h"
#include "pebble_fonts.h"


#define MY_UUID { 0xAC, 0xA3, 0xB3, 0xD0, 0xBF, 0x4A, 0x47, 0x77, 0x92, 0x38, 0xFF, 0x95, 0xF0, 0x7A, 0xA2, 0x21 }
PBL_APP_INFO(MY_UUID,
             "Pebble Sifter", "thompsnm",
             0, 1, /* App version */
             DEFAULT_MENU_ICON,
             APP_INFO_STANDARD_APP);

const int vert_scroll_text_padding = 4;

const int header_display_height = 16;

const int sifter_name_layer_vert_size = 20;

const char *sifter_names[2];
sifter_names[0] = "Team Trivia";
sifter_names[1] = "Hartmann";

const char *sifter_full_names[2];
sifter_full_names[0] = "Team Trivia Free Answer";
sifter_full_names[1] = "Hartmann Game Status";

static struct MainScreenData {
  Window window;
  ScrollLayer sifter_text_scroll_layer;
  TextLayer sifter_name_layer;
  TextLayer sifter_text_layer;
  AppSync sync;
  uint8_t sync_buffer[128];
} main_screen_data;

static struct SifterMenuData {
  Window window;
  SimpleMenuLayer simple_menu_layer;
  SimpleMenuSection menu_sections[1];
  SimpleMenuItem menu_items[2];
} sifter_menu_data;

enum {
  SIFTER_PEBBLE_NAME_KEY = 0x0,    // TUPLE_CSTRING
  SIFTER_TEXT_KEY = 0x1,    // TUPLE_CSTRING
  SIFTER_FULL_NAME_KEY = 0x2,  // TUPLE_CSTRING
};

// TODO: Error handling
static void sync_error_callback(DictionaryResult dict_error, AppMessageResult app_message_error, void *context) {
}

static void sync_tuple_changed_callback(const uint32_t key, const Tuple* new_tuple, const Tuple* old_tuple, void* context) {
  const GRect max_text_bounds = GRect(0, 0, 144, 2000);
  switch (key) {
  case SIFTER_PEBBLE_NAME_KEY:
    text_layer_set_text(&main_screen_data.sifter_name_layer, new_tuple->value->cstring);
    break;
  case SIFTER_TEXT_KEY:
    scroll_layer_set_content_size(&main_screen_data.sifter_text_scroll_layer, max_text_bounds.size);
    text_layer_set_text(&main_screen_data.sifter_text_layer, new_tuple->value->cstring);
    GSize max_size = text_layer_get_max_used_size(app_get_current_graphics_context(), &main_screen_data.sifter_text_layer);
    text_layer_set_size(&main_screen_data.sifter_text_layer, max_size);
    scroll_layer_set_content_size(&main_screen_data.sifter_text_scroll_layer, GSize(144, max_size.h + vert_scroll_text_padding));
    break;
  default:
    return;
  }
}

static void send_cmd(char sifter_select[]) {
  Tuplet value = TupletCString(SIFTER_FULL_NAME_KEY, sifter_select);
  
  DictionaryIterator *iter;
  app_message_out_get(&iter);
  
  if (iter == NULL)
    return;
  
  dict_write_tuplet(iter, &value);
  dict_write_end(iter);
  
  app_message_out_send();
  app_message_out_release();
}

static void handle_deinit(AppContextRef c) {
  app_sync_deinit(&main_screen_data.sync);
}

void menu_select_callback(int index, void *ctx) {
  Window* window = &sifter_menu_data.window;
  window_stack_pop(window);
  send_cmd(sifter_full_names[index]);
}

void sifter_menu_init() {
  // Initialize the menu window
  Window* window = &sifter_menu_data.window;
  window_init(window, "Sifter Menu");
  window_stack_push(window, true /* Animated */ );

  int num_a_items = 0;

  // Set up the menu items
  sifter_menu_data.menu_items[num_a_items++] = (SimpleMenuItem){
    .title = sifter_name_0,
    .callback = menu_select_callback,
  };

  sifter_menu_data.menu_items[num_a_items++] = (SimpleMenuItem){
    .title = sifter_name_1,
    .callback = menu_select_callback,
  };

  // Bind the menu items to the corresponding menu sections
  sifter_menu_data.menu_sections[0] = (SimpleMenuSection){
    .num_items = 2,
    .items = sifter_menu_data.menu_items,
  };

  GRect bounds = window->layer.bounds;

  // Initialize the simple menu layer
  simple_menu_layer_init(&sifter_menu_data.simple_menu_layer, bounds, window, sifter_menu_data.menu_sections, 1, NULL);

  // Add it to the window for display
  layer_add_child(&window->layer, simple_menu_layer_get_layer(&sifter_menu_data.simple_menu_layer));
}

void select_single_click_handler(ClickRecognizerRef recognizer, Window *window) {
  sifter_menu_init();
}

void click_config_provider(ClickConfig **config, Window *window) {
  config[BUTTON_ID_SELECT]->click.handler = (ClickHandler) select_single_click_handler;
}

void main_screen_handle_init(AppContextRef ctx) {
  const GRect max_text_bounds = GRect(0, 0, 144, 2000);

  Tuplet initial_values[] = {
    TupletCString(SIFTER_PEBBLE_NAME_KEY, "Sifter Name"),
    TupletCString(SIFTER_TEXT_KEY, "Sifted Text"),
  };

  // Initialize the main screen window
  Window* window = &main_screen_data.window;
  window_init(window, "Pebble Sifter");
  window_stack_push(window, true /* Animated */ );

  // Initialize the sifter name layer and add it to the window
  text_layer_init(&main_screen_data.sifter_name_layer, GRect(0, 0, 144, sifter_name_layer_vert_size));
  text_layer_set_text_alignment(&main_screen_data.sifter_name_layer, GTextAlignmentCenter);
  // TODO: This should be pulled from initial_values
  text_layer_set_text(&main_screen_data.sifter_name_layer, "Sifter Name");
  layer_add_child(&window->layer, &main_screen_data.sifter_name_layer.layer);

  // Initialize the scroll layer
  scroll_layer_init(&main_screen_data.sifter_text_scroll_layer, window->layer.bounds);
  scroll_layer_init(&main_screen_data.sifter_text_scroll_layer, GRect(0, sifter_name_layer_vert_size, 144, (168 - sifter_name_layer_vert_size - header_display_height)));
  // Looks like this doesn't play well with window_set_click_config_provider
  // Commenting it out until I can dig into it further
//  scroll_layer_set_click_config_onto_window(&main_screen_data.sifter_text_scroll_layer, window);
  scroll_layer_set_content_size(&main_screen_data.sifter_text_scroll_layer, max_text_bounds.size);

  // Initialize the sifter text layer
  text_layer_init(&main_screen_data.sifter_text_layer, max_text_bounds);
  // TODO: This should be pulled from initial_values
  text_layer_set_text(&main_screen_data.sifter_text_layer, "Sifted Text");

  // Trim text layer and scroll content to fit text box
  GSize max_size = text_layer_get_max_used_size(app_get_current_graphics_context(), &main_screen_data.sifter_text_layer);
  text_layer_set_size(&main_screen_data.sifter_text_layer, max_size);
  scroll_layer_set_content_size(&main_screen_data.sifter_text_scroll_layer, GSize(144, max_size.h + vert_scroll_text_padding));

  // Add the sifter text layer and scroll layer to the window
  scroll_layer_add_child(&main_screen_data.sifter_text_scroll_layer, &main_screen_data.sifter_text_layer.layer);
  layer_add_child(&window->layer, &main_screen_data.sifter_text_scroll_layer.layer);

  // Initialize select button config
  window_set_click_config_provider(window, (ClickConfigProvider) click_config_provider);

  // Initialize AppSync
  app_sync_init(&main_screen_data.sync, main_screen_data.sync_buffer, sizeof(main_screen_data.sync_buffer), initial_values, ARRAY_LENGTH(initial_values), sync_tuple_changed_callback, sync_error_callback, NULL);
}

void pbl_main(void *params) {
  PebbleAppHandlers handlers = {
    .init_handler = &main_screen_handle_init,
    .deinit_handler = &handle_deinit,
    .messaging_info = {
      .buffer_sizes = {
        .inbound = 1024,
        .outbound = 1024,
      }
    }
  };
  app_event_loop(params, &handlers);
}

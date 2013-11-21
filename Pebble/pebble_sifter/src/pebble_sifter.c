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

static struct PebbleSifterData {
  Window window;
  ScrollLayer sifter_text_scroll_layer;
  TextLayer sifter_name_layer;
  TextLayer sifter_text_layer;
  AppSync sync;
  uint8_t sync_buffer[128];
} s_data;

enum {
  SIFTER_NAME_KEY = 0x0,    // TUPLE_CSTRING
  SIFTER_TEXT_KEY = 0x1,    // TUPLE_CSTRING
};

// TODO: Error handling
static void sync_error_callback(DictionaryResult dict_error, AppMessageResult app_message_error, void *context) {
}

static void sync_tuple_changed_callback(const uint32_t key, const Tuple* new_tuple, const Tuple* old_tuple, void* context) {
  switch (key) {
  case SIFTER_NAME_KEY:
    text_layer_set_text(&s_data.sifter_name_layer, new_tuple->value->cstring);
    break;
  case SIFTER_TEXT_KEY:
    text_layer_set_text(&s_data.sifter_text_layer, new_tuple->value->cstring);
    GSize max_size = text_layer_get_max_used_size(app_get_current_graphics_context(), &s_data.sifter_text_layer);
    text_layer_set_size(&s_data.sifter_text_layer, max_size);
    scroll_layer_set_content_size(&s_data.sifter_text_scroll_layer, GSize(144, max_size.h + vert_scroll_text_padding));
    break;
  default:
    return;
  }
}

void handle_init(AppContextRef ctx) {
  const GRect max_text_bounds = GRect(0, 0, 144, 2000);

  Tuplet initial_values[] = {
    TupletCString(SIFTER_NAME_KEY, "Sifter Name"),
    TupletCString(SIFTER_TEXT_KEY, "Sifted Text"),
  };

  // Initialize the window
  Window* window = &s_data.window;
  window_init(window, "Pebble Sifter");
  window_stack_push(window, true /* Animated */ );

  // Initialize the sifter name layer and add it to the window
  text_layer_init(&s_data.sifter_name_layer, GRect(0, 0, 144, 20));
  text_layer_set_text_alignment(&s_data.sifter_name_layer, GTextAlignmentCenter);
  text_layer_set_text(&s_data.sifter_name_layer, "Sifter Name");
  layer_add_child(&window->layer, &s_data.sifter_name_layer.layer);

  // Initialize the scroll layer
  scroll_layer_init(&s_data.sifter_text_scroll_layer, GRect(0, 20, 144, 168));
  scroll_layer_set_click_config_onto_window(&s_data.sifter_text_scroll_layer, window);
  scroll_layer_set_content_size(&s_data.sifter_text_scroll_layer, max_text_bounds.size);

  // Initialize the sifter text layer
  text_layer_init(&s_data.sifter_text_layer, max_text_bounds);
  text_layer_set_text(&s_data.sifter_text_layer, "Sifted Text");

  // Trim text layer and scroll content to fit text box
  GSize max_size = text_layer_get_max_used_size(app_get_current_graphics_context(), &s_data.sifter_text_layer);
  text_layer_set_size(&s_data.sifter_text_layer, max_size);
  scroll_layer_set_content_size(&s_data.sifter_text_scroll_layer, GSize(144, max_size.h + vert_scroll_text_padding));

  // Add the sifter text layer and scroll layer to the window
  scroll_layer_add_child(&s_data.sifter_text_scroll_layer, &s_data.sifter_text_layer.layer);
  layer_add_child(&window->layer, &s_data.sifter_text_scroll_layer.layer);

  app_sync_init(&s_data.sync, s_data.sync_buffer, sizeof(s_data.sync_buffer), initial_values, ARRAY_LENGTH(initial_values), sync_tuple_changed_callback, sync_error_callback, NULL);
}

static void handle_deinit(AppContextRef c) {
  app_sync_deinit(&s_data.sync);
}


void pbl_main(void *params) {
  PebbleAppHandlers handlers = {
    .init_handler = &handle_init,
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

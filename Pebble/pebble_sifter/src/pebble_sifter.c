#include "pebble_os.h"
#include "pebble_app.h"
#include "pebble_fonts.h"


#define MY_UUID { 0xAC, 0xA3, 0xB3, 0xD0, 0xBF, 0x4A, 0x47, 0x77, 0x92, 0x38, 0xFF, 0x95, 0xF0, 0x7A, 0xA2, 0x21 }
PBL_APP_INFO(MY_UUID,
             "Pebble Sifter", "thompsnm",
             0, 1, /* App version */
             DEFAULT_MENU_ICON,
             APP_INFO_STANDARD_APP);

Window window;
TextLayer sifter_name_layer;
TextLayer sifter_text_layer;

static struct PebbleSifterData {
  AppSync sync;
  uint8_t sync_buffer[32];
} s_data;

enum {
  SIFTER_NAME_KEY = 0x0,    // TUPLE_CSTRING
  SIFTER_TEXT_KEY = 0x1,    // TUPLE_CSTRING
};

// TODO: Error handling
static void sync_error_callback(DictionaryResult dict_error, AppMessageResult app_message_error, void *context) {
}

static void sync_tuple_changed_callback(const uint32_t key, const Tuple* new_tuple, const Tuple* old_tuple, void* context) {

//  switch (key) {
//  case SIFTER_NAME_KEY:
//    text_layer_set_text(&sifter_name_layer, new_tuple->value->cstring);
//    break;
//  case SIFTER_TEXT_KEY:
//    text_layer_set_text(&sifter_text_layer, new_tuple->value->cstring);
//    break;
//  default:
//    return;
//  }
}

void handle_init(AppContextRef ctx) {

  window_init(&window, "Pebble Sifter");
  window_stack_push(&window, true /* Animated */);
  text_layer_init(&sifter_name_layer, GRect(0, 0, 144, 20));
  text_layer_set_text_alignment(&sifter_name_layer, GTextAlignmentCenter);
  text_layer_set_text(&sifter_name_layer, "Sifter Name");
  layer_add_child(&window.layer, &sifter_name_layer.layer);
  text_layer_init(&sifter_text_layer, GRect(0, 20, 144, 168));
  text_layer_set_text(&sifter_text_layer, "Sifted Text");
  layer_add_child(&window.layer, &sifter_text_layer.layer);

  Tuplet initial_values[] = {
    TupletCString(SIFTER_NAME_KEY, "Sifter Name"),
    TupletCString(SIFTER_TEXT_KEY, "Sifted Text"),
  };
  app_sync_init(&s_data.sync, s_data.sync_buffer, sizeof(s_data.sync_buffer), initial_values, ARRAY_LENGTH(initial_values), sync_tuple_changed_callback, sync_error_callback, NULL);
}


void pbl_main(void *params) {
  PebbleAppHandlers handlers = {
    .init_handler = &handle_init,
    .messaging_info = {
      .buffer_sizes = {
        .inbound = 256,
        .outbound = 256,
      }
    }
  };
  app_event_loop(params, &handlers);
}

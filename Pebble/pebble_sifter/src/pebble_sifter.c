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

void handle_init(AppContextRef ctx) {

  window_init(&window, "Pebble Sifter");
  window_stack_push(&window, true /* Animated */);
  text_layer_init(&sifter_name_layer, GRect(0, 0, 144, 30));
  text_layer_set_text_alignment(&sifter_name_layer, GTextAlignmentCenter);
  text_layer_set_text(&sifter_name_layer, "Sifter Name");
  layer_add_child(&window.layer, &sifter_name_layer.layer);
  text_layer_init(&sifter_text_layer, GRect(0, 30, 144, 168));
  text_layer_set_text(&sifter_text_layer, "Sifted Text");
  layer_add_child(&window.layer, &sifter_text_layer.layer);
}


void pbl_main(void *params) {
  PebbleAppHandlers handlers = {
    .init_handler = &handle_init
  };
  app_event_loop(params, &handlers);
}

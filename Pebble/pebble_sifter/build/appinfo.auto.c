#include "pebble_app_info.h"
#include "src/resource_ids.auto.h"

const PebbleAppInfo __pbl_app_info __attribute__ ((section (".pbl_header"))) = {
  .header = "PBLAPP",
  .struct_version = { APP_INFO_CURRENT_STRUCT_VERSION_MAJOR, APP_INFO_CURRENT_STRUCT_VERSION_MINOR },
  .sdk_version = { APP_INFO_CURRENT_SDK_VERSION_MAJOR, APP_INFO_CURRENT_SDK_VERSION_MINOR },
  .app_version = { 0, 1 },
  .load_size = 0xb6b6,
  .offset = 0xb6b6b6b6,
  .crc = 0xb6b6b6b6,
  .name = "Pebble Sifter",
  .company = "thompsnm",
  .icon_resource_id = RESOURCE_ID_SIFTER_PEBBLE_ICON,
  .sym_table_addr = 0xA7A7A7A7,
  .flags = 0,
  .num_reloc_entries = 0xdeadcafe,
  .uuid = { 0xAC, 0xA3, 0xB3, 0xD0, 0xBF, 0x4A, 0x47, 0x77, 0x92, 0x38, 0xFF, 0x95, 0xF0, 0x7A, 0xA2, 0x21 },
  .virtual_size = 0xb6b6
};

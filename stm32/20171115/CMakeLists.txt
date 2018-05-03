cmake_minimum_required(VERSION 3.0)

project(stm32)

set(CMAKE_VERBOSE_MAKEFILE OFF)

enable_language(ASM)

set(ELF_FILE "${PROJECT_NAME}.elf")
set(BIN_FILE "${PROJECT_NAME}.bin")
set(HEX_FILE "${PROJECT_NAME}.hex")


add_definitions(-DSTM32F10X_MD -DUSE_STDPERIPH_DRIVER)

set(COMPILER_FLAGS " -W -Wall -Os")
set(COMPILER_FLAGS "${COMPILER_FLAGS} -mthumb -mcpu=cortex-m3")
set(COMPILER_FLAGS "${COMPILER_FLAGS} -ffunction-sections")
set(COMPILER_FLAGS "${COMPILER_FLAGS} -fdata-sections")

set(LINKER_FLAGS " --specs=nosys.specs")
set(LINKER_FLAGS "${LINKER_FLAGS} -Wl,--gc-sections")
set(LINKER_FLAGS "${LINKER_FLAGS} -T ${PROJECT_SOURCE_DIR}/flash.ld")

set(CMAKE_C_FLAGS ${COMPILER_FLAGS} CACHE INTERNAL "")
set(CMAKE_CXX_FLAGS ${COMPILER_FLAGS} CACHE INTERNAL "")
set(CMAKE_EXE_LINKER_FLAGS ${LINKER_FLAGS} CACHE INTERNAL "")


include_directories(core/inc)

file(GLOB SOURCES core/src/*.c core/startup/*.s src/*.c)

add_executable(${ELF_FILE} ${SOURCES})

add_custom_target(objcopy ALL
	COMMAND ${TOOLCHAIN_SIZE} ${ELF_FILE}
	COMMAND ${TOOLCHAIN_OBJCOPY} ${ELF_FILE} ${BIN_FILE} -Obinary
	COMMAND ${TOOLCHAIN_OBJCOPY} ${ELF_FILE} ${HEX_FILE} -Oihex
	DEPENDS ${ELF_FILE}
)

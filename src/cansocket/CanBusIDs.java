/* CanBusIDs - CAN bus message interface
 * This file was automatically generated
 * from canMessageInput.txt on Sun Aug 10 21:26:03 PDT 2003
 * Do not hand-edit.
 *
 * All constants defined public static final.
 */
package cansocket;

public interface CanBusIDs
{
    public static final String genTime = {"Sun Aug 10 21:26:03 PDT 2003"};

// --- from C header file aps_exports.h ---

// from c enum: aps_can_id_t	java: ApsCanId

    public static final int can_id_aps_set_reset = 304;	// 0x130
    public static final int can_id_aps_report_error = 257;	// 0x101
    public static final int can_id_aps_set_state = 306;	// 0x132
    public static final int can_id_aps_report_state = 258;	// 0x102
    public static final int can_id_aps_get_state = 298;	// 0x12a
    public static final int can_id_umb_set_rocketready = 368;	// 0x170
    public static final int can_id_umb_report_rocketready = 320;	// 0x140
    public static final int can_id_umb_get_rocketready = 360;	// 0x168
    public static final int can_id_umb_report_connection = 321;	// 0x141
    public static final int can_id_umb_get_connection = 361;	// 0x169
    public static final int can_id_umb_report_voltage = 322;	// 0x142
    public static final int can_id_umb_get_voltage = 362;	// 0x16a
    public static final int can_id_umb_set_isoc_messages = 371;	// 0x173
    public static final int can_id_pwr_set_isoc_messages = 435;	// 0x1b3
    public static final int can_id_pwr_report_voltage = 384;	// 0x180
    public static final int can_id_pwr_get_voltage = 424;	// 0x1a8
    public static final int can_id_pwr_report_current = 385;	// 0x181
    public static final int can_id_pwr_get_current = 425;	// 0x1a9
    public static final int can_id_pwr_report_charge = 386;	// 0x182
    public static final int can_id_pwr_get_charge = 426;	// 0x1aa
    public static final int can_id_pwr_set_charge = 434;	// 0x1b2

// from c enum: aps_state_t	java: ApsState

    public static final int aps_state_sleep = 0;
    public static final int aps_state_waking_up = 1;
    public static final int aps_state_shutting_down = 2;
    public static final int aps_state_on = 3;

// from c enum: aps_error_t	java: ApsError

    public static final int NOT_AN_ERROR_MESSAGE = 0;
    public static final int UNKNOWN_APS_COMMAND = 1;
    public static final int INVALID_APS_RESET_COMMAND = 2;
    public static final int APS_UNKNOWN_COMMAND_TYPE = 3;
    public static final int APS_MISSED_10HZ_TICK = 4;
    public static final int NOT_VALID_UMB_COMMAND = 5;
    public static final int RR_ABORTED_DUE_TO_VALID_VUMB = 6;
    public static final int INVALID_ROCKET_READY_COMMAND = 7;
    public static final int INVALID_UMB_COMMAND = 8;
    public static final int UNKNOWN_PWR_COMMAND = 9;
    public static final int INVALID_APS_SET_STATE_COMMAND = 10;	// 0xa
    public static final int APS_SHUTDOWN_MSG_EQ_ZERO = 11;	// 0xb
    public static final int APS_SHUTDOWN_BAD_LENGTH = 12;	// 0xc
    public static final int FE_BAD_APS_STATE = 13;	// 0xd

// --- from C header file Bps_exports.h ---

// from c enum: aps_enum_t	java: ApsEnum

    public static final int ENUM0_MESSAGE = 0;
    public static final int ENUM1_MESSAGE = 291;	// 0x123
    public static final int ENUM2_MESSAGE = 123;	// 0x7b

}	// end interface CanBusIDs

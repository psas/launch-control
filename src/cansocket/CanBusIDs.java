package cansocket;
public interface CanBusIDs {
	public static final int CID_REQUEST = 0x0010; /* from lv2_id_macros */
	public static final int CID_SEND = 0x0000; /* from lv2_id_macros */
	public static final int CID_BC_MASK = 0x000f; /* from lv2_id_macros */
	public static final int CID_BC_DATA = 0x0000; /* from lv2_id_macros */
	public static final int CID_BC_DISABLE = 0x0000; /* from lv2_id_macros */
	public static final int CID_BC_ENABLE = 0x0001; /* from lv2_id_macros */
	public static final int CID_MASK_DLC = 0xfff0; /* from lv2_id_macros */
	public static final int CID_GET_DLC = 0x000f; /* from lv2_id_macros */
	public static final int CID_DLC_0 = 0x0000; /* from lv2_id_macros */
	public static final int CID_DLC_1 = 0x0001; /* from lv2_id_macros */
	public static final int CID_DLC_2 = 0x0002; /* from lv2_id_macros */
	public static final int CID_DLC_3 = 0x0003; /* from lv2_id_macros */
	public static final int CID_DLC_4 = 0x0004; /* from lv2_id_macros */
	public static final int CID_DLC_5 = 0x0005; /* from lv2_id_macros */
	public static final int CID_DLC_6 = 0x0006; /* from lv2_id_macros */
	public static final int CID_DLC_7 = 0x0007; /* from lv2_id_macros */
	public static final int CID_DLC_8 = 0x0008; /* from lv2_id_macros */
	public static final int CID_DLC_UNKNOWN = 0x0000; /* from lv2_id_macros */
	public static final int CID_ACTION = 0x0000; /* from lv2_id_macros */
	public static final int CID_ACTION_BC = 0x0010; /* from lv2_id_macros */
	public static final int CID_TEST = 0x0110; /* from lv2_id_macros */
	public static final int CID_INFO = 0x0100; /* from lv2_id_macros */
	public static final int CID_REPORT = 0x0100; /* from lv2_id_macros */
	public static final int CID_RESPOND = 0x0100; /* from lv2_id_macros */
	public static final int CID_EVENT = 0x0100; /* from lv2_id_macros */
	public static final int CID_ERROR = 0x0100; /* from lv2_id_macros */
	public static final int CID_SET = 0x0200; /* from lv2_id_macros */
	public static final int CID_ACK = 0x0210; /* from lv2_id_macros */
	public static final int CID_CONTROL = 0x0200; /* from lv2_id_macros */
	public static final int CID_GET = 0x0310; /* from lv2_id_macros */
	public static final int CID_DATA = 0x0300; /* from lv2_id_macros */
	public static final int CID_ENABLE = 0x0310; /* from lv2_id_macros */
	public static final int FC_NID = 0x0000; /* from lv2_node_ids */
	public static final int REC_NID = 0x0800; /* from lv2_node_ids */
	public static final int LTR_NID = 0x1000; /* from lv2_node_ids */
	public static final int IMU_NID = 0x1800; /* from lv2_node_ids */
	public static final int APS_NID = 0x2800; /* from lv2_node_ids */
	public static final int UMB_NID = 0x3000; /* from lv2_node_ids */
	public static final int PWR_NID = 0x3800; /* from lv2_node_ids */
	public static final int GPS_NID = 0x4800; /* from lv2_node_ids */
	public static final int GPS_UART_NID = 0x5000; /* from lv2_node_ids */
	public static final int PRESS_NID = 0x6000; /* from lv2_node_ids */
	public static final int TEMP_NID = 0x6800; /* from lv2_node_ids */
	public static final int ATV_NID = 0x7000; /* from lv2_node_ids */
	public static final int ATV_UART_NID = 0x7800; /* from lv2_node_ids */
	public static final int FC_IMU_NID = 0xf000; /* from lv2_node_ids */
	public static final int FC_GPS_NID = 0xf800; /* from lv2_node_ids */
	public static final int APS_MODE_SLEEP = 0x0012; /* from aps/aps_exports */
	public static final int APS_MODE_AWAKE = 0x0023; /* from aps/aps_exports */
	public static final int APS_MODE_SAFE = 0x0034; /* from aps/aps_exports */
	public static final int APS_MODE_ARMED = 0x0088; /* from aps/aps_exports */
	public static final int APS_MODE = 0x0000; /* from aps/aps_exports */
	public static final int APS_RESET = 0x0020; /* from aps/aps_exports */
	public static final int APS_ERROR = 0x0040; /* from aps/aps_exports */
	public static final int APS_SWITCH_1 = 0x0060; /* from aps/aps_exports */
	public static final int APS_SWITCH_2 = 0x0080; /* from aps/aps_exports */
	public static final int APS_SWITCH_3 = 0x00a0; /* from aps/aps_exports */
	public static final int APS_SWITCH_4 = 0x00c0; /* from aps/aps_exports */
	public static final int UMB_CONNECTOR = 0x0000; /* from aps/aps_exports */
	public static final int UMB_SHOREPOWER = 0x0020; /* from aps/aps_exports */
	public static final int UMB_ROCKETREADY = 0x0040; /* from aps/aps_exports */
	public static final int PWR_VOLTS = 0x0000; /* from aps/aps_exports */
	public static final int PWR_AMPS = 0x0020; /* from aps/aps_exports */
	public static final int PWR_CHARGE = 0x0040; /* from aps/aps_exports */
	public static final int PWR_CHARGER = 0x0060; /* from aps/aps_exports */
	public static final int APS_RESET_NODE = 0x2821; /* from aps/aps_exports */
	public static final int APS_SET_ERROR = 0x2841; /* from aps/aps_exports */
	public static final int APS_REPORT_ERROR = 0x2944; /* from aps/aps_exports */
	public static final int APS_SET_MODE = 0x2801; /* from aps/aps_exports */
	public static final int APS_GET_MODE = 0x2b10; /* from aps/aps_exports */
	public static final int APS_REPORT_MODE = 0x2b01; /* from aps/aps_exports */
	public static final int APS_SET_SWITCH_1 = 0x2861; /* from aps/aps_exports */
	public static final int APS_GET_SWITCH_1 = 0x2b70; /* from aps/aps_exports */
	public static final int APS_REPORT_SWITCH_1 = 0x2b61; /* from aps/aps_exports */
	public static final int APS_SET_SWITCH_2 = 0x2881; /* from aps/aps_exports */
	public static final int APS_GET_SWITCH_2 = 0x2b90; /* from aps/aps_exports */
	public static final int APS_REPORT_SWITCH_2 = 0x2b81; /* from aps/aps_exports */
	public static final int APS_SET_SWITCH_3 = 0x28a1; /* from aps/aps_exports */
	public static final int APS_GET_SWITCH_3 = 0x2bb0; /* from aps/aps_exports */
	public static final int APS_REPORT_SWITCH_3 = 0x2ba1; /* from aps/aps_exports */
	public static final int APS_SET_SWITCH_4 = 0x28c1; /* from aps/aps_exports */
	public static final int APS_GET_SWITCH_4 = 0x2bd0; /* from aps/aps_exports */
	public static final int APS_REPORT_SWITCH_4 = 0x2bc1; /* from aps/aps_exports */
	public static final int UMB_GET_SHORE_POWER = 0x3330; /* from aps/aps_exports */
	public static final int UMB_REPORT_SHORE_POWER = 0x3321; /* from aps/aps_exports */
	public static final int UMB_GET_CONNECTOR = 0x3310; /* from aps/aps_exports */
	public static final int UMB_REPORT_CONNECTOR = 0x3301; /* from aps/aps_exports */
	public static final int UMB_SET_ROCKETREADY = 0x3241; /* from aps/aps_exports */
	public static final int UMB_GET_ROCKETREADY = 0x3350; /* from aps/aps_exports */
	public static final int UMB_REPORT_ROCKETREADY = 0x3341; /* from aps/aps_exports */
	public static final int PWR_REPORT_CHARGER = 0x3b61; /* from aps/aps_exports */
	public static final int APS_ENABLE_VOLTS = 0x3b10; /* from aps/aps_exports */
	public static final int APS_DATA_VOLTS = 0x3b02; /* from aps/aps_exports */
	public static final int APS_ENABLE_AMPS = 0x3b30; /* from aps/aps_exports */
	public static final int APS_DATA_AMPS = 0x3b24; /* from aps/aps_exports */
	public static final int APS_ENABLE_CHARGE = 0x3b50; /* from aps/aps_exports */
	public static final int APS_DATA_CHARGE = 0x3b42; /* from aps/aps_exports */
	public static final int ATV_MODE_SAFE = 0x0034; /* from atv/atv_exports */
	public static final int ATV_MODE_ARMED = 0x0088; /* from atv/atv_exports */
	public static final int ATV_MODE = 0x0000; /* from atv/atv_exports */
	public static final int ATV_RESET = 0x0020; /* from atv/atv_exports */
	public static final int ATV_ERROR = 0x0040; /* from atv/atv_exports */
	public static final int ATV_CAMERA_POWER_SWITCH = 0x0060; /* from atv/atv_exports */
	public static final int ATV_OVERLAY_POWER_SWITCH = 0x0080; /* from atv/atv_exports */
	public static final int ATV_TX_POWER_SWITCH = 0x00a0; /* from atv/atv_exports */
	public static final int ATV_PA_POWER_SWITCH = 0x00c0; /* from atv/atv_exports */
	public static final int ATV_UART_RX = 0x0000; /* from atv/atv_exports */
	public static final int ATV_UART_ERROR = 0x0020; /* from atv/atv_exports */
	public static final int ATV_UART_CONFIG = 0x0040; /* from atv/atv_exports */
	public static final int GET_DLC_MASK = 0x000f; /* from atv/atv_exports */
	public static final int ATV_RESET_NODE = 0x7021; /* from atv/atv_exports */
	public static final int ATV_SET_ERROR = 0x7041; /* from atv/atv_exports */
	public static final int ATV_REPORT_ERROR = 0x7144; /* from atv/atv_exports */
	public static final int ATV_SET_MODE = 0x7001; /* from atv/atv_exports */
	public static final int ATV_GET_MODE = 0x7310; /* from atv/atv_exports */
	public static final int ATV_REPORT_MODE = 0x7301; /* from atv/atv_exports */
	public static final int ATV_SET_POWER_CAMERA = 0x7061; /* from atv/atv_exports */
	public static final int ATV_GET_POWER_CAMERA = 0x7370; /* from atv/atv_exports */
	public static final int ATV_REPORT_POWER_CAMERA = 0x7361; /* from atv/atv_exports */
	public static final int ATV_SET_POWER_OVERLAY = 0x7081; /* from atv/atv_exports */
	public static final int ATV_GET_POWER_OVERLAY = 0x7390; /* from atv/atv_exports */
	public static final int ATV_REPORT_POWER_OVERLAY = 0x7381; /* from atv/atv_exports */
	public static final int ATV_SET_POWER_TX = 0x70a1; /* from atv/atv_exports */
	public static final int ATV_GET_POWER_TX = 0x73b0; /* from atv/atv_exports */
	public static final int ATV_REPORT_POWER_TX = 0x73a1; /* from atv/atv_exports */
	public static final int ATV_SET_POWER_PA = 0x70c1; /* from atv/atv_exports */
	public static final int ATV_GET_POWER_PA = 0x73d0; /* from atv/atv_exports */
	public static final int ATV_REPORT_POWER_PA = 0x73c1; /* from atv/atv_exports */
	public static final int ATV_UART_RECIEVE = 0x7b00; /* from atv/atv_exports */
	public static final int FC_STATE = 0x0020; /* from fc_exports */
	public static final int FC_ABORT = 0x0040; /* from fc_exports */
	public static final int FC_STATE_DETAIL = 0x0060; /* from fc_exports */
	public static final int FC_LINK_QUALITY = 0x0080; /* from fc_exports */
	public static final int FC_REPORT_STATE = 0x0121; /* from fc_exports */
	public static final int FC_FORCE_STATE = 0x0221; /* from fc_exports */
	public static final int FC_REQUEST_STATE = 0x0021; /* from fc_exports */
	public static final int FC_ABORT_LAUNCH = 0x0040; /* from fc_exports */
	public static final int FC_REPORT_STATE_DETAIL = 0x0164; /* from fc_exports */
	public static final int FC_FORCE_STATE_DETAIL = 0x0264; /* from fc_exports */
	public static final int FC_REPORT_LINK_QUALITY = 0x0184; /* from fc_exports */
	public static final int FC_IMU_HEIGHT = 0xf004; /* from fc_exports */
	public static final int FC_GPS_NAVSOL = 0xf804; /* from fc_exports */
	public static final int FC_GPS_TIME = 0xf827; /* from fc_exports */
	public static final int FC_GPS_LATLON = 0xf848; /* from fc_exports */
	public static final int FC_GPS_HEIGHT = 0xf864; /* from fc_exports */
	public static final int FC_GPS_SATS_VIS = 0xf881; /* from fc_exports */
	public static final int FC_GPS_SATS_USED = 0xf8a1; /* from fc_exports */
	public static final int EvaluatePowerupState = 0x0000; /* from fc_exports */
	public static final int InitializeState = 0x0001; /* from fc_exports */
	public static final int IdleState = 0x0002; /* from fc_exports */
	public static final int PreflightCheckState = 0x0003; /* from fc_exports */
	public static final int ReadyState = 0x0004; /* from fc_exports */
	public static final int ArmingState = 0x0005; /* from fc_exports */
	public static final int ArmedState = 0x0006; /* from fc_exports */
	public static final int RocketReadyState = 0x0007; /* from fc_exports */
	public static final int LaunchAbortState = 0x0008; /* from fc_exports */
	public static final int BoostState = 0x0009; /* from fc_exports */
	public static final int CoastState = 0x000a; /* from fc_exports */
	public static final int DeployDrogueState = 0x000b; /* from fc_exports */
	public static final int DescendDrogueState = 0x000c; /* from fc_exports */
	public static final int DeployMainState = 0x000d; /* from fc_exports */
	public static final int DescendMainState = 0x000e; /* from fc_exports */
	public static final int RecoveryWaitState = 0x000f; /* from fc_exports */
	public static final int RecoverySleepState = 0x0010; /* from fc_exports */
	public static final int PowerDownState = 0x0011; /* from fc_exports */
	public static final int LawnDartState = 0x0012; /* from fc_exports */
	public static final int GPS_POWER_OFF = 0x0000; /* from gps/gps_exports */
	public static final int GPS_POWER_ON = 0x0001; /* from gps/gps_exports */
	public static final int GPS_MODE_SAFE = 0x0034; /* from gps/gps_exports */
	public static final int GPS_MODE_ARMED = 0x0088; /* from gps/gps_exports */
	public static final int GPS_NMEA_PROTOCOL = 0x0000; /* from gps/gps_exports */
	public static final int GPS_BINARY_PROTOCOL = 0x0001; /* from gps/gps_exports */
	public static final int GPS_USE_ROM = 0x0000; /* from gps/gps_exports */
	public static final int GPS_USE_EEPROM = 0x0001; /* from gps/gps_exports */
	public static final int GPS_MODE = 0x0000; /* from gps/gps_exports */
	public static final int GPS_RESET = 0x0020; /* from gps/gps_exports */
	public static final int GPS_ERROR = 0x0040; /* from gps/gps_exports */
	public static final int GPS_POWER = 0x0060; /* from gps/gps_exports */
	public static final int GPS_PROTOCOL = 0x0080; /* from gps/gps_exports */
	public static final int GPS_ROM = 0x00a0; /* from gps/gps_exports */
	public static final int GPS_1HZ = 0x00c0; /* from gps/gps_exports */
	public static final int GPS_10KHZ = 0x00e0; /* from gps/gps_exports */
	public static final int GPS_UART_TX = 0x0000; /* from gps/gps_exports */
	public static final int GPS_UART_RX = 0x0020; /* from gps/gps_exports */
	public static final int GPS_UART_ERROR = 0x0040; /* from gps/gps_exports */
	public static final int GPS_UART_CONFIG = 0x0060; /* from gps/gps_exports */
	public static final int GPS_RESET_NODE = 0x4821; /* from gps/gps_exports */
	public static final int GPS_SET_ERROR = 0x4841; /* from gps/gps_exports */
	public static final int GPS_REPORT_ERROR = 0x4944; /* from gps/gps_exports */
	public static final int GPS_SET_MODE = 0x4801; /* from gps/gps_exports */
	public static final int GPS_GET_MODE = 0x4b10; /* from gps/gps_exports */
	public static final int GPS_REPORT_MODE = 0x4b01; /* from gps/gps_exports */
	public static final int GPS_SET_POWER = 0x4861; /* from gps/gps_exports */
	public static final int GPS_GET_POWER = 0x4b70; /* from gps/gps_exports */
	public static final int GPS_REPORT_POWER = 0x4b61; /* from gps/gps_exports */
	public static final int GPS_SET_PROTOCOL = 0x4881; /* from gps/gps_exports */
	public static final int GPS_GET_PROTOCOL = 0x4b90; /* from gps/gps_exports */
	public static final int GPS_REPORT_PROTOCOL = 0x4b81; /* from gps/gps_exports */
	public static final int GPS_SET_ROM = 0x48a1; /* from gps/gps_exports */
	public static final int GPS_GET_ROM = 0x4bb0; /* from gps/gps_exports */
	public static final int GPS_REPORT_ROM = 0x4ba1; /* from gps/gps_exports */
	public static final int GPS_1HZ_MESSAGE = 0x4bc0; /* from gps/gps_exports */
	public static final int GPS_ENABLE_1HZ_MESSAGE = 0x4ac0; /* from gps/gps_exports */
	public static final int GPS_10KHZ_MESSAGE = 0x4be0; /* from gps/gps_exports */
	public static final int GPS_ENABLE_10KHZ_MESSAGE = 0x4ae0; /* from gps/gps_exports */
	public static final int GPS_ENABLE_UART_TRANSMIT = 0x5310; /* from gps/gps_exports */
	public static final int GPS_UART_TRANSMIT = 0x5300; /* from gps/gps_exports */
	public static final int GPS_UART_RECEIVE = 0x5320; /* from gps/gps_exports */
	public static final int IMU_MODE_SAFE = 0x0034; /* from imu/imu_exports */
	public static final int IMU_MODE_ARMED = 0x0088; /* from imu/imu_exports */
	public static final int IMU_RESET = 0x0000; /* from imu/imu_exports */
	public static final int IMU_ERROR = 0x0020; /* from imu/imu_exports */
	public static final int IMU_MODE = 0x0060; /* from imu/imu_exports */
	public static final int ACCEL_DATA = 0x0080; /* from imu/imu_exports */
	public static final int GYRO_DATA = 0x00a0; /* from imu/imu_exports */
	public static final int PRESS_ERROR = 0x0000; /* from imu/imu_exports */
	public static final int PRESS_DATA = 0x0020; /* from imu/imu_exports */
	public static final int TEMP_ERROR = 0x0000; /* from imu/imu_exports */
	public static final int TEMP_DATA = 0x0020; /* from imu/imu_exports */
	public static final int IMU_RESET_NODE = 0x1801; /* from imu/imu_exports */
	public static final int IMU_SET_ERROR = 0x1821; /* from imu/imu_exports */
	public static final int IMU_REPORT_ERROR = 0x1924; /* from imu/imu_exports */
	public static final int IMU_SET_MODE = 0x1861; /* from imu/imu_exports */
	public static final int IMU_GET_MODE = 0x1b70; /* from imu/imu_exports */
	public static final int IMU_REPORT_MODE = 0x1b61; /* from imu/imu_exports */
	public static final int IMU_ENABLE_ACCEL = 0x1b90; /* from imu/imu_exports */
	public static final int IMU_ACCEL_DATA = 0x1b88; /* from imu/imu_exports */
	public static final int IMU_ENABLE_GYRO = 0x1bb0; /* from imu/imu_exports */
	public static final int IMU_GYRO_DATA = 0x1ba6; /* from imu/imu_exports */
	public static final int PRESS_ENABLE_DATA = 0x6330; /* from imu/imu_exports */
	public static final int PRESS_REPORT_DATA = 0x6322; /* from imu/imu_exports */
	public static final int TEMP_ENABLE_DATA = 0x6b30; /* from imu/imu_exports */
	public static final int TEMP_REPORT_DATA = 0x6b22; /* from imu/imu_exports */
	public static final int LTR_MODE_SAFE = 0x0034; /* from ltr/ltr_exports */
	public static final int LTR_MODE_ARMED = 0x0088; /* from ltr/ltr_exports */
	public static final int LTR_RESET = 0x0000; /* from ltr/ltr_exports */
	public static final int LTR_ERROR = 0x0020; /* from ltr/ltr_exports */
	public static final int LTR_MODE = 0x0060; /* from ltr/ltr_exports */
	public static final int LTR_IGNITION = 0x0080; /* from ltr/ltr_exports */
	public static final int LTR_SIREN = 0x00a0; /* from ltr/ltr_exports */
	public static final int LTR_STROBE = 0x00c0; /* from ltr/ltr_exports */
	public static final int LTR_SPOWER = 0x00e0; /* from ltr/ltr_exports */
	public static final int LTR_RESET_NODE = 0x1001; /* from ltr/ltr_exports */
	public static final int LTR_SET_ERROR = 0x1021; /* from ltr/ltr_exports */
	public static final int LTR_REPORT_ERROR = 0x1124; /* from ltr/ltr_exports */
	public static final int LTR_SET_MODE = 0x1061; /* from ltr/ltr_exports */
	public static final int LTR_GET_MODE = 0x1370; /* from ltr/ltr_exports */
	public static final int LTR_REPORT_MODE = 0x1361; /* from ltr/ltr_exports */
	public static final int LTR_SET_SIREN = 0x10a1; /* from ltr/ltr_exports */
	public static final int LTR_GET_SIREN = 0x13b0; /* from ltr/ltr_exports */
	public static final int LTR_REPORT_SIREN = 0x13a1; /* from ltr/ltr_exports */
	public static final int LTR_SET_STROBE = 0x10c1; /* from ltr/ltr_exports */
	public static final int LTR_GET_STROBE = 0x13d0; /* from ltr/ltr_exports */
	public static final int LTR_REPORT_STROBE = 0x13c1; /* from ltr/ltr_exports */
	public static final int LTR_SET_SPOWER = 0x10e1; /* from ltr/ltr_exports */
	public static final int LTR_GET_SPOWER = 0x13f0; /* from ltr/ltr_exports */
	public static final int LTR_REPORT_SPOWER = 0x13e1; /* from ltr/ltr_exports */
	public static final int LTR_SET_IGNITION = 0x1083; /* from ltr/ltr_exports */
	public static final int LTR_GET_IGNITION = 0x1390; /* from ltr/ltr_exports */
	public static final int LTR_REPORT_IGNITION = 0x1383; /* from ltr/ltr_exports */
	public static final int REC_MODE_SAFE = 0x00e3; /* from recovery/recovery_exports */
	public static final int REC_MODE_ARMED_2M = 0x0056; /* from recovery/recovery_exports */
	public static final int REC_MODE_ARMED = 0x007a; /* from recovery/recovery_exports */
	public static final int REC_MODE_ABORT = 0x0066; /* from recovery/recovery_exports */
	public static final int REC_PYRO_FIRE_KEY = 0x00f1; /* from recovery/recovery_exports */
	public static final int REC_PYRO = 0x0000; /* from recovery/recovery_exports */
	public static final int REC_TIMER = 0x0020; /* from recovery/recovery_exports */
	public static final int REC_MODE = 0x0040; /* from recovery/recovery_exports */
	public static final int REC_RESET = 0x0060; /* from recovery/recovery_exports */
	public static final int REC_ERROR = 0x0080; /* from recovery/recovery_exports */
	public static final int REC_DTMF = 0x00a0; /* from recovery/recovery_exports */
	public static final int REC_RESET_NODE = 0x0861; /* from recovery/recovery_exports */
	public static final int REC_SET_ERROR = 0x0881; /* from recovery/recovery_exports */
	public static final int REC_REPORT_ERROR = 0x0984; /* from recovery/recovery_exports */
	public static final int REC_SET_MODE = 0x0841; /* from recovery/recovery_exports */
	public static final int REC_GET_MODE = 0x0b50; /* from recovery/recovery_exports */
	public static final int REC_REPORT_MODE = 0x0b41; /* from recovery/recovery_exports */
	public static final int REC_SET_PYRO = 0x0802; /* from recovery/recovery_exports */
	public static final int REC_REPORT_PYRO = 0x0902; /* from recovery/recovery_exports */
	public static final int REC_SET_TIMER = 0x0a23; /* from recovery/recovery_exports */
	public static final int REC_GET_TIMER = 0x0b30; /* from recovery/recovery_exports */
	public static final int REC_REPORT_TIMER = 0x0923; /* from recovery/recovery_exports */
	public static final int REC_REPORT_DTMF = 0x09a3; /* from recovery/recovery_exports */
}

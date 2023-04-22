package seniorproject;

// https://stackoverflow.com/a/28486560

import java.util.Map;

public class ControllerMediator
{
    private FXMLController FXMLController;
    private SettingsController settingsController;

    void registerFXMLController(FXMLController controller) {
        FXMLController = controller;
    }

    void registerSettingsController(SettingsController controller) {
        settingsController = controller;
    }

    void FXMLControllerUpdateSettings( Map<String, String> settings ) {
         FXMLController.updateSettings( settings );
    }

//    void controller3OperateOn(String data) {
//        controller3.operateOn(data);
//    }

    /**
     * Everything below here is in support of Singleton pattern
     */
    private ControllerMediator() {}

    public static ControllerMediator getInstance() {
        return ControllerMediatorHolder.INSTANCE;
    }

    private static class ControllerMediatorHolder {
        private static final ControllerMediator INSTANCE = new ControllerMediator();
    }
}

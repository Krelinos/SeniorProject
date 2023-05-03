package seniorproject;

// https://stackoverflow.com/a/28486560

import java.util.Map;

public class ControllerMediator
{
    private FXMLController FXMLController;
    private SettingsController settingsController;

    void registerFXMLController(FXMLController controller) {
        System.out.println("fxl set");
        FXMLController = controller;
    }

    void registerSettingsController(SettingsController controller) {
        System.out.println("set");
        settingsController = controller;
    }

    void FXMLControllerUpdateSettings( Map<String, String> settings ) {
         FXMLController.updateSettings( settings );
    }
    
    Object SettingsControllerGetSetting( String setting )
    {
        return settingsController.settings.get( setting );
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

package educationalappstore.eas.services.licensing;

// service interface
oneway interface IEasLicensingResultListener {
    void checkUserAppPermission(int response_code, String message, String signature);
}
package educationalappstore.eas.services.licensing;

import educationalappstore.eas.services.licensing.IEasLicensingResultListener;
// service interface
oneway interface IEasLicensingService {
    //sample method
    void checkUserAppPermission(String app_key,in IEasLicensingResultListener listener );
}
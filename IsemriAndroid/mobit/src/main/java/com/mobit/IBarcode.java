package com.mobit;


/**
 * Created by Genel on 12.10.2018.
 */

public interface IBarcode {

    void startScanner(IForm form);
    BarcodeResult getScanResult(int requestCode, int resultCode, Object context);

}

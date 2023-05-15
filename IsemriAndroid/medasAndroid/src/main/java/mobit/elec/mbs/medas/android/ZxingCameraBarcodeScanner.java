package mobit.elec.mbs.medas.android;

import android.app.Activity;
import android.content.Intent;

import com.google.zxing.client.android.CaptureActivity;
import com.mobit.BarcodeResult;
import com.mobit.IBarcode;
import com.mobit.IForm;


/**
 * Created by Genel on 12.10.2018.
 */

public class ZxingCameraBarcodeScanner implements IBarcode {

    private static final int SCANNER_REQUEST_CODE = 1001;

    public void startScanner(IForm form)
    {
        Activity activity = (Activity)form;
        Intent intent = new Intent(activity.getApplicationContext(), CaptureActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        activity.startActivityForResult(intent, SCANNER_REQUEST_CODE);
    }

    public BarcodeResult getScanResult(int requestCode, int resultCode, Object context)
    {
        BarcodeResult result = new BarcodeResult();

        if(requestCode != SCANNER_REQUEST_CODE)
            return result;

        if (resultCode == Activity.RESULT_OK) {
            // Handle successful scan
            Intent intent = (Intent)context;
            result.resultData = intent.getStringExtra("SCAN_RESULT");
            String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
            byte[] rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
            int intentOrientation = intent.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
            Integer orientation = (intentOrientation == Integer.MIN_VALUE) ? null : intentOrientation;
            String errorCorrectionLevel = intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");
            result.resultStatus = BarcodeResult.SUCCESS;

        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            result.resultStatus = BarcodeResult.CANCELLED;
        }
        return result;
    }
}

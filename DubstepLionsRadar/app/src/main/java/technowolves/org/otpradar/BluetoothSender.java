package technowolves.org.otpradar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothSender extends BroadcastReceiver {

    public static final int REQUEST_ENABLE_BT = 18;

    private BluetoothAdapter mAdapter;
    private Context mContext;

    private void setUp(Activity activity) {

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(mContext, "Device does not support Bluetooth",
                    Toast.LENGTH_LONG).show();
        }

        if(!mAdapter.isEnabled()) {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(btIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (((int)extras.get(BluetoothAdapter.EXTRA_STATE)) == BluetoothAdapter.STATE_ON) {
            new BluetoothAccept().start();
        }
    }

    private class BluetoothAccept extends Thread {

        private final BluetoothServerSocket mServerSocket;
        private final UUID uuid = UUID.randomUUID();

        public BluetoothAccept() {

            // Use a temporary object that is later assigned to mmServerSocket, because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord("DubstepLions Radar",
                        uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mServerSocket = tmp;

        }

        @Override
        public void run() {
            BluetoothSocket socket = null;

            while (true) {

                try {
                    socket = mServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                if (socket != null) {
                    new BluetoothConnect(socket);
                    cancel();
                    break;
                }

            }

        }

        private void cancel() {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class BluetoothConnect extends Thread {

        private final BluetoothSocket mSocket;
        private final InputStream mInStream;
        private final OutputStream mOutStream;

        public BluetoothConnect(BluetoothSocket socket) {
            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mInStream = tmpIn;
            mOutStream = tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while(true) {
                try {
                    bytes = mInStream.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mOutStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

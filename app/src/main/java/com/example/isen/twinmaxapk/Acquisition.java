package com.example.isen.twinmaxapk;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import com.example.isen.twinmaxapk.bleSercive.utils.DataContainer;
import com.example.isen.twinmaxapk.bleSercive.utils.DecodeFrameAsyncTask;
import com.example.isen.twinmaxapk.bleSercive.utils.DecoderListener;
import com.example.isen.twinmaxapk.bleSercive.utils.RawContainer;
import com.example.isen.twinmaxapk.bleService.activities_frags.BTScanActivity;
import com.example.isen.twinmaxapk.database.Measure;
import com.example.isen.twinmaxapk.database.RealmMeasure;
import com.example.isen.twinmaxapk.database.fragments.PopupDeleteMaintenanceFragment;
import com.example.isen.twinmaxapk.database.fragments.PopupSaveHistoric;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import android.os.Handler;
import android.widget.Toast;

import java.nio.channels.Pipe;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
//import java.util.logging.Handler;
import java.util.logging.LogRecord;

import io.realm.RealmList;

public class Acquisition extends Activity  {

    private static LineData pointsDur;

    //popup
    PopupSaveHistoric popupSaveHistoric;

    //Decoder fields
    private RawContainer mRawContainer;
    private DecodeFrameAsyncTask mDecoder;
    private DataContainer mCleanData;
    private List<Measure> subMeasure = new ArrayList<>(200);

    //Scheudled task for graph refreshing !
    private Timer mTimer;
    private int refreshDelay = 400;
    private boolean mustRefresh = true;


    //Conv fatctor  = - 315)/1.837
    private final int DIF_FACTOR = 315;
    private final double CONV_FACTOR = 1.837;
    private final int FILER_TRIGGER = 400;

    public ObservableArrayList.OnListChangedCallback mCleanDataCallback = new ObservableList.OnListChangedCallback() {
        public int changeCounter = 0;

        @Override
        public void onChanged(ObservableList sender) {

        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {

        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            if(isStop) {
                return;
            }
            changeCounter++;
            //Log.w("TEST", "TEST");
           // Log.e("taille", "val: "+sender.size());
                if (mCleanData.getSizeOfList() >= 600 && mustRefresh && changeCounter >= 200) {

                /*if(sender.size() >= 200) {
                    subMeasure.clear();
                    for(int i=0; i<200;i++) {
                        subMeasure.add(new Measure((Measure) sender.get(0)));
                        sender.remove(0);
                    }
                }*/

                    mustRefresh = false;
                    changeCounter = 0;
                   // Log.e("UPDATE GRAPH", "REfreshing GRAPH !");
                    updateGraphs();
                    //Log.w("Update Graph", "Graph starts update !");
                }

        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {

        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {

        }
    };
    public ObservableArrayList.OnListChangedCallback mDecoderCallback = new ObservableList.OnListChangedCallback() {
        @Override
        public void onChanged(ObservableList sender) {

        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {

        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
           //Log.e("value added !","value added");
            if(mDecoder == null || mDecoder.getStatus().compareTo(AsyncTask.Status.FINISHED) == 0) {
                mDecoder = new DecodeFrameAsyncTask(mDocedListener);
                mDecoder.execute(mRawContainer);
            }
        } 

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {

        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {

        }
    };


    private DecoderListener mDocedListener = new DecoderListener() {
        @Override
        public void addCleanData(Measure measure) {
            if(mCleanData != null) {
                mCleanData.addValue(measure);
            }
        }
    };


    //Ble fields
    public final static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public final static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private String mDeviceAdrress;
    private String mDeviceName;

    private boolean mConnected = false;
    private TextView mConnectionState;
    private Button saveHistorique;
    //End of Ble fields


    private DecoView arcView;
    private int serie1Index;
    private int maxValue = 5000;
    private int minValue = 0;
    private int data = 1500;

    ArrayList<String> labelsInit = new ArrayList<String>();
    ArrayList<Entry> data0 = new ArrayList<Entry>();
    ArrayList<Entry> data1 = new ArrayList<Entry>();
    ArrayList<Entry> data2 = new ArrayList<Entry>();
    ArrayList<Entry> data3 = new ArrayList<Entry>();
    ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
    LineChart chart;
    static int nbrPoints = 200;
    ArrayList<Measure> MeasuresList = new ArrayList<>();
    static int valeurButton = 150;

    private Button mStopButton;
    private boolean isStop = false;

    //BT normal service vars
    private BTService mBTService = null;

    private BluetoothAdapter mBluetoothAdapter;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BTService.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            Log.e("Connection établie", "connection was established !");
                            mConnectionState.setText(R.string.connected);
                            break;
                        case BTService.STATE_CONNECTING:

                            break;
                        case BTService.STATE_LISTEN:
                        case BTService.STATE_NONE:

                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if(!isStop) {
                        mRawContainer.addFrame(readBuf, msg.arg1);
                    }

                    //for (int i=0;i<msg.arg1;i++) {
                        //Log.e("buf", "value:"+readBuf[i]);
                    //}

                   // Log.e("data received : ", "value : " + readBuf.toString());
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name


                    break;
                case Constants.MESSAGE_TOAST:

                    break;
            }
        }
    };

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(BTScanActivity.EXTRA_DEVICE_ADDRESS);
        mDeviceAdrress = address;
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        // Attempt to connect to the device
        mBTService.connect(device, secure);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acquisition);

        mTimer = new Timer();

        //Setup Decoder
        mRawContainer = new RawContainer(mDecoderCallback);
        mDecoder = null;

        mCleanData = new DataContainer(mCleanDataCallback);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBTService = new BTService(getApplicationContext(), mHandler);
        final Intent intent = getIntent();
        mDeviceAdrress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        connectDevice(intent, false);

        mConnectionState = (TextView) findViewById(R.id.acquisition_connection_state);
        saveHistorique = (Button) findViewById(R.id.acquisition_save_historique);

        chart = new LineChart(this);
        chart = (LineChart) findViewById(R.id.chart);
        //init labels
        for (int i = 0; i <= nbrPoints; i++) {
            labelsInit.add("");
        }
        fillMeasuresList();

        //Mise à jour des data0,1,2,3
        for (int i = 0; i <= nbrPoints; i++) {
            data0.add(new Entry(MeasuresList.get(i).get(0), i));
            data1.add(new Entry(MeasuresList.get(i).get(1), i));
            data2.add(new Entry(MeasuresList.get(i).get(2), i));
            data3.add(new Entry(MeasuresList.get(i).get(3), i));
        }
        //Mise à jour de dataset
        lines.add(new LineDataSet(data0, "data0"));
        lines.add(new LineDataSet(data1, "data1"));
        lines.add(new LineDataSet(data2, "data2"));
        lines.add(new LineDataSet(data3, "data3"));
        //Mettre data dans chart
        chart.setData(new LineData(labelsInit, lines));


        //options générales
        chart.getAxisRight().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.setDescription("Pression (mBar)");
        chart.setTouchEnabled(true);
        //options data0
        chart.getLineData().getDataSetByIndex(0).setDrawCubic(true);
        chart.getLineData().getDataSetByIndex(0).setValueTextSize(0);
        chart.getLineData().getDataSetByIndex(0).setDrawCircles(false);
        chart.getLineData().getDataSetByIndex(0).setColor(Color.rgb(237, 127, 16));
        //options data1
        chart.getLineData().getDataSetByIndex(1).setDrawCubic(true);
        chart.getLineData().getDataSetByIndex(1).setValueTextSize(0);
        chart.getLineData().getDataSetByIndex(1).setDrawCircles(false);
        chart.getLineData().getDataSetByIndex(1).setColor(Color.rgb(58, 142, 186));
        //options data2
        chart.getLineData().getDataSetByIndex(2).setDrawCubic(true);
        chart.getLineData().getDataSetByIndex(2).setValueTextSize(0);
        chart.getLineData().getDataSetByIndex(2).setDrawCircles(false);
        chart.getLineData().getDataSetByIndex(2).setColor(Color.rgb(127, 221, 76));
        //options data3
        chart.getLineData().getDataSetByIndex(3).setDrawCubic(true);
        chart.getLineData().getDataSetByIndex(3).setValueTextSize(0);
        chart.getLineData().getDataSetByIndex(3).setDrawCircles(false);
        chart.getLineData().getDataSetByIndex(3).setColor(Color.rgb(247, 35, 12));

        //checkbox
        final CheckBox c0 = (CheckBox) findViewById(R.id.c0);
        c0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c0 = (CheckBox) findViewById(R.id.c0);
                if (c0.isChecked() == true) {
                    chart.getLineData().getDataSetByIndex(0).setVisible(true);
                } else {
                    chart.getLineData().getDataSetByIndex(0).setVisible(false);
                }
            }
        });

        final CheckBox c1 = (CheckBox) findViewById(R.id.c1);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c1 = (CheckBox) findViewById(R.id.c1);
                if (c1.isChecked() == true) {
                    chart.getLineData().getDataSetByIndex(1).setVisible(true);
                } else {
                    chart.getLineData().getDataSetByIndex(1).setVisible(false);
                }
            }
        });

        final CheckBox c2 = (CheckBox) findViewById(R.id.c2);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c2 = (CheckBox) findViewById(R.id.c2);
                if (c2.isChecked() == true) {
                    chart.getLineData().getDataSetByIndex(2).setVisible(true);
                } else {
                    chart.getLineData().getDataSetByIndex(2).setVisible(false);
                }
            }
        });


        final CheckBox c3 = (CheckBox) findViewById(R.id.c3);
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c3 = (CheckBox) findViewById(R.id.c3);
                if (c3.isChecked() == true) {
                    chart.getLineData().getDataSetByIndex(3).setVisible(true);
                } else {
                    chart.getLineData().getDataSetByIndex(3).setVisible(false);
                }
            }
        });
        //options checkbox
        c0.setTextColor(chart.getLineData().getDataSetByIndex(0).getColor());
        c1.setTextColor(chart.getLineData().getDataSetByIndex(1).getColor());
        c2.setTextColor(chart.getLineData().getDataSetByIndex(2).getColor());
        c3.setTextColor(chart.getLineData().getDataSetByIndex(3).getColor());

        //AutoScale
        Button b0 = (Button) findViewById(R.id.b0);
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chart.fitScreen();
            }
        });

        saveHistorique.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                RealmList<RealmMeasure> measuresSaved = new RealmList<RealmMeasure>();
                List<Entry> entry0 = chart.getLineData().getDataSetByIndex(0).getYVals();
                List<Entry> entry1 = chart.getLineData().getDataSetByIndex(1).getYVals();
                List<Entry> entry2 = chart.getLineData().getDataSetByIndex(2).getYVals();
                List<Entry> entry3 = chart.getLineData().getDataSetByIndex(3).getYVals();

                for(int i=0;i<entry0.size() && i<entry1.size() && i<entry2.size() && i< entry3.size(); i++) {
                    measuresSaved.add(new RealmMeasure((int)entry0.get(i).getVal(),(int)entry1.get(i).getVal(),(int)entry2.get(i).getVal(),(int)entry3.get(i).getVal()));
                }

                Maintenance maintenance = new Maintenance(null,null);
                maintenance.setMeasures(measuresSaved);

                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                popupSaveHistoric = new PopupSaveHistoric(getApplication());
                popupSaveHistoric.show(transaction, "test");
            }
        });

        // compte tour
        Button buttonMoins = (Button) findViewById(R.id.moins);
        buttonMoins.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                data = data - 500;
                arcView.addEvent(new DecoEvent.Builder(data).setIndex(serie1Index).setDelay(0).setDuration(0).build());
                TextView valeur = (TextView) findViewById(R.id.valeur);
                valeur.setText(Integer.toString(data));

                // +
                if (valeurButton > 0) {
                    valeurButton--;
                }
            }
        });

        //Button buttonPlus = (Button) findViewById(R.id.plus);
        /*buttonPlus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                data = data + 500;
                arcView.addEvent(new DecoEvent.Builder(data).setIndex(serie1Index).setDelay(0).setDuration(0).build());
                TextView valeur = (TextView) findViewById(R.id.valeur);
                valeur.setText(Integer.toString(data));

                // -
                if (valeurButton < 150) {
                    valeurButton++;
                }
            }
        });*/

        arcView = (DecoView) findViewById(R.id.arcView);

        //create data series
        createBackSerie();
        createDataSerie1();

        //setup events
        createEvents();
        isStop = false;
        mStopButton = (Button)findViewById(R.id.moins);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStop) {
                    isStop = false;
                    mStopButton.setText("Stop");
                } else {
                    isStop = true;
                    mStopButton.setText("Start");
                }
            }
        });

    }

    protected void onResume() {
        super.onResume();
        if(mBTService != null && mDeviceAdrress != null && mBluetoothAdapter != null) {
            if(mBTService.getState() == BTService.STATE_NONE) {
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mDeviceAdrress);
                mBTService.connect(device, false);
            }
        }
    }
    //private int mOscTrigger = 1650;
    private int mOscTrigger = 0;
    private final int DELTA_TRIGGER = 50;
    private void copyValToSub() {
        synchronized (subMeasure) {
            int i = 0;
            int tryCounter = 0;
            boolean found = false;
            while (!found) {
                i = 0;
                tryCounter++;

                subMeasure.clear();
                subMeasure = mCleanData.getGraphValues();
                if (tryCounter >= 5) {
                    fillSubMeasureCorrectly(200);
                    //Log.e("Didn'tfind","Couldn't find trigger point ! ");
                    findOscTrigger();
                    return;

                }
                if (subMeasure.size() != 200) {
                    continue;
                }
                if (mOscTrigger != 0) {
                    while (i < 200 && !found) {
                        if (subMeasure.get(0) != null) {
                            if ((subMeasure.get(0).get(0) >= mOscTrigger - DELTA_TRIGGER) && (subMeasure.get(0).get(0) <= mOscTrigger + DELTA_TRIGGER)) {
                                found = true;
                                fillSubMeasureCorrectly(i);
                            } else {
                                subMeasure.remove(0);
                            }
                        } else {
                            subMeasure.remove(0);
                        }
                        i++;
                    }
                } else {
                    mOscTrigger = subMeasure.get(0).get(0);

                    return;
                }
            }
            findOscTrigger();
        }
    }

    private void findOscTrigger() {
        int max = 0;
        for(Measure m:subMeasure) {
            if(m != null) {
                if (m.get(0) > max) {
                    max = m.get(0);
                }
            }
        }
        mOscTrigger = max;
    }

    private void smoothenSubMeasureIntoMeasureList() {

        for(int i=2;i<nbrPoints - 2;i++) {
            if (i < subMeasure.size()) {
                if (subMeasure.get(i) != null) {
                   // Log.e("Adding a value","ADDING A VALUE INTO BEACH");
                    MeasuresList.get(i-2).setC0((subMeasure.get(i-2).get(0) + subMeasure.get(i-1).get(0) + subMeasure.get(i).get(0) + subMeasure.get(i+1).get(0) + subMeasure.get(i+2).get(0))/5);
                    MeasuresList.get(i-2).setC1((subMeasure.get(i-2).get(1) + subMeasure.get(i-1).get(1) + subMeasure.get(i).get(1) + subMeasure.get(i+1).get(1) + subMeasure.get(i+2).get(1))/5);
                    MeasuresList.get(i-2).setC2((subMeasure.get(i-2).get(2) + subMeasure.get(i-1).get(2) + subMeasure.get(i).get(2) + subMeasure.get(i+1).get(2) + subMeasure.get(i+2).get(2))/5);
                    MeasuresList.get(i-2).setC3((subMeasure.get(i-2).get(3) + subMeasure.get(i-1).get(3) + subMeasure.get(i).get(3) + subMeasure.get(i+1).get(3) + subMeasure.get(i+2).get(3))/5);
                }
            }
        }
    }


    private void fillSubMeasureCorrectly(int startingCorrectFrame) {
        //Log.e("Value deleted","Value DELETED : " + startingCorrectFrame + " ; value to ADD : " );
        for(int i=0;i<startingCorrectFrame;i++) {
            /*if(subMeasure.size() >0) {
                subMeasure.remove(0);
            }*/
            if(subMeasure.size() < 200) {
                subMeasure.add(mCleanData.getFirst());
            }
        }
        //Log.e("SubMeasure size", "After DELETION : " + subMeasure.size());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBTService.stop();
        finish();
    }
    private Thread mThread;
    private void updateGraphs() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                copyValToSub();
                //nbrPoints = 200;
                nbrPoints = subMeasure.size();
                synchronized (subMeasure) {
                    Log.e("NBRpoint", "Taile subMeasure : " + subMeasure.size());
                    for (int i = 0; i < nbrPoints; i++) {
                        if (i < subMeasure.size()) {
                            if (subMeasure.get(i) != null) {
                                MeasuresList.get(i).setC0(subMeasure.get(i).get(0));
                                MeasuresList.get(i).setC1(subMeasure.get(i).get(1));
                                MeasuresList.get(i).setC2(subMeasure.get(i).get(2));
                                MeasuresList.get(i).setC3(subMeasure.get(i).get(3));
                            }
                        }
                    }
                    //smoothenSubMeasureIntoMeasureList();
                }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //nbrPoints = valeurButton;
                            ////copyValToSub();
                            //nbrPoints = subMeasure.size();
                            ////nbrPoints = 200;
                            //nbrPoints = subMeasure.size();
                            /*if(nbrPoints <200) {
                                return;
                            }*/

                            /*synchronized (subMeasure) {
                                Log.e("NBRpoint", "Taile subMeasure : " + nbrPoints);
                                // addItemsAtTheEnd(nbrPoints); //Fais automatiquement normalement
                                //removeItems(nbrPoints);
                                //Log.w("Update Graph", "Graph starts update !");
                                //Log.w("Size of sublist : ", "value : " + subMeasure.size());
                                for (int i = 0; i < nbrPoints; i++) {
                                    if (i < subMeasure.size()) {
                                        if (subMeasure.get(i) != null) {
                                            MeasuresList.get(i).setC0(subMeasure.get(i).get(0));
                                            MeasuresList.get(i).setC1(subMeasure.get(i).get(1));
                                            MeasuresList.get(i).setC2(subMeasure.get(i).get(2));
                                            MeasuresList.get(i).setC3(subMeasure.get(i).get(3));
                                        }
                                    }
                                }
                            }*/
                            //Log.w("Update Graph", "Graph starts update !");
                            data0.clear();
                            data1.clear();
                            data2.clear();
                            data3.clear();

                            float val0 = MeasuresList.get(0).get(0);
                            float val1 = MeasuresList.get(0).get(1);
                            float val2 = MeasuresList.get(0).get(2);
                            float val3 = MeasuresList.get(0).get(3);
                            Log.e("MeasureList","Size : " + MeasuresList.size() + " ; First value : " + val0 + ";" + val1 + ";" + val2 + ";" + val3 + ";");
                            for(int i = 1; i < nbrPoints-1; i++){

                                /*float v0 = (MeasuresList.get(i-2).get(0) + MeasuresList.get(i-1).get(0) + MeasuresList.get(i).get(0) + MeasuresList.get(i+1).get(0) + MeasuresList.get(i+2).get(0))/5;
                                float v1 = (MeasuresList.get(i-2).get(1) + MeasuresList.get(i-1).get(1) + MeasuresList.get(i).get(1) + MeasuresList.get(i+1).get(1) + MeasuresList.get(i+2).get(1))/5;
                                float v2 = (MeasuresList.get(i-2).get(2) + MeasuresList.get(i-1).get(2) + MeasuresList.get(i).get(2) + MeasuresList.get(i+1).get(2) + MeasuresList.get(i+2).get(2))/5;
                                float v3 = (MeasuresList.get(i-2).get(3) + MeasuresList.get(i-1).get(3) + MeasuresList.get(i).get(3) + MeasuresList.get(i+1).get(3) + MeasuresList.get(i+2).get(3))/5;


                                data0.add(new Entry((float)((v0 - DIF_FACTOR)/CONV_FACTOR),i-2));
                                data1.add(new Entry((float)((v1 - DIF_FACTOR)/CONV_FACTOR),i-2));
                                data2.add(new Entry((float)((v2 - DIF_FACTOR)/CONV_FACTOR),i-2));
                                data3.add(new Entry((float)((v3 - DIF_FACTOR)/CONV_FACTOR),i-2));*/



                                if(Math.abs(MeasuresList.get(i).get(0) - val0) >= FILER_TRIGGER){
                                    MeasuresList.get(i).setC0((int)val0);
                                }
                                data0.add(new Entry((float)((MeasuresList.get(i-1).get(0)-DIF_FACTOR)/CONV_FACTOR),i-1));
                                val0 = MeasuresList.get(i).get(0);

                                if(Math.abs(MeasuresList.get(i).get(1) - val1) >= FILER_TRIGGER){
                                    MeasuresList.get(i).setC1((int)val1);
                                }
                                data1.add(new Entry((float)((MeasuresList.get(i-1).get(1)-DIF_FACTOR)/CONV_FACTOR),i-1));
                                val1 = MeasuresList.get(i).get(1);

                                if(Math.abs(MeasuresList.get(i).get(2) - val2) >= FILER_TRIGGER){
                                    MeasuresList.get(i).setC2((int)val2);
                                }
                                data2.add(new Entry((float)((MeasuresList.get(i-1).get(2)-315)/CONV_FACTOR),i-1));
                                val2 = MeasuresList.get(i).get(2);

                                if(Math.abs(MeasuresList.get(i).get(3) - val3) >= FILER_TRIGGER){
                                    MeasuresList.get(i).setC3((int)val3);
                                }
                                data3.add(new Entry((float)((MeasuresList.get(i-1).get(3)-315)/CONV_FACTOR),i-1));
                                val3 = MeasuresList.get(i).get(3);
                            }
                            //Log.w("Update Graph", "Graph starts update !");
                            chart.notifyDataSetChanged();
                            chart.invalidate();
                            mustRefresh = true;
                            Log.e("Can refresh", "CAN REFRESH !!");
                            //Log.w("Update Graph", "Graph starts update !");
                        }
                    });

            }
        });
        mThread.start();
    }


    @Override
    protected void onPause() {
        mBTService.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mBTService.stop();
        super.onDestroy();
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    public void removeItems(int points) {
        if (MeasuresList.size() >= points) {
            MeasuresList.subList(0, points).clear();
        }
    }

    public void addItemsAtTheEnd(int points) {
        for (int i = 0; i <= points - 1; i++) {
            int m0 = MeasuresList.get(i).get(0);
            int m1 = MeasuresList.get(i).get(1);
            int m2 = MeasuresList.get(i).get(2);
            int m3 = MeasuresList.get(i).get(3);
            Measure m = new Measure(m0, m1, m2, m3);
            MeasuresList.add(m);
        }
    }

    public void fillMeasuresList() {
        Measure measure1 = new Measure(100);
        Measure measure2 = new Measure(100);
        Measure measure3 = new Measure(100);
        Measure measure4 = new Measure(100);
        Measure measure5 = new Measure(100);
        Measure measure6 = new Measure(100);
        Measure measure7 = new Measure(100);
        Measure measure8 = new Measure(101);
        Measure measure9 = new Measure(100);
        Measure measure10 = new Measure(100);
        Measure measure11 = new Measure(102);
        Measure measure12 = new Measure(101);
        Measure measure13 = new Measure(100);
        Measure measure14 = new Measure(100);
        Measure measure15 = new Measure(100);
        Measure measure16 = new Measure(100);
        Measure measure17 = new Measure(100);
        Measure measure18 = new Measure(100);
        Measure measure19 = new Measure(99);
        Measure measure20 = new Measure(100);
        Measure measure21 = new Measure(100);
        Measure measure22 = new Measure(100);
        Measure measure23 = new Measure(98);
        Measure measure24 = new Measure(100);
        Measure measure25 = new Measure(101);
        Measure measure26 = new Measure(100);
        Measure measure27 = new Measure(100);
        Measure measure28 = new Measure(101);
        Measure measure29 = new Measure(100);
        Measure measure30 = new Measure(100);
        Measure measure31 = new Measure(100);
        Measure measure32 = new Measure(100);
        Measure measure33 = new Measure(100);
        Measure measure34 = new Measure(100);
        Measure measure35 = new Measure(100);
        Measure measure36 = new Measure(100);
        Measure measure37 = new Measure(100);
        Measure measure38 = new Measure(101);
        Measure measure39 = new Measure(100);
        Measure measure40 = new Measure(99);
        Measure measure41 = new Measure(98);
        Measure measure42 = new Measure(100);
        Measure measure43 = new Measure(101);
        Measure measure44 = new Measure((int)((2250-315)/CONV_FACTOR));
        Measure measure45 = new Measure(100);
        Measure measure46 = new Measure(100);
        Measure measure47 = new Measure(100);
        Measure measure48 = new Measure(99);
        Measure measure49 = new Measure(100);
        Measure measure50 = new Measure(100);
        Measure measure51 = new Measure(99);
        Measure measure52 = new Measure(98);
        Measure measure53 = new Measure(97);
        Measure measure54 = new Measure(96);
        Measure measure55 = new Measure(95);
        Measure measure56 = new Measure(94);
        Measure measure57 = new Measure(92);
        Measure measure58 = new Measure(90);
        Measure measure59 = new Measure(85);
        Measure measure60 = new Measure(82);
        Measure measure61 = new Measure(77);
        Measure measure62 = new Measure(70);
        Measure measure63 = new Measure(65);
        Measure measure64 = new Measure(55);
        Measure measure65 = new Measure(50);
        Measure measure66 = new Measure(45);
        Measure measure67 = new Measure(40);
        Measure measure68 = new Measure(35);
        Measure measure69 = new Measure(30);
        Measure measure70 = new Measure(25);
        Measure measure71 = new Measure(23);
        Measure measure72 = new Measure(21);
        Measure measure73 = new Measure(19);
        Measure measure74 = new Measure(18);
        Measure measure75 = new Measure(17);
        Measure measure76 = new Measure(16);
        Measure measure77 = new Measure(15);
        Measure measure78 = new Measure(15);
        Measure measure79 = new Measure(15);
        Measure measure80 = new Measure(14);
        Measure measure81 = new Measure(14);
        Measure measure82 = new Measure(14);
        Measure measure83 = new Measure(14);
        Measure measure84 = new Measure(13);
        Measure measure85 = new Measure(13);
        Measure measure86 = new Measure(14);
        Measure measure87 = new Measure(14);
        Measure measure88 = new Measure(15);
        Measure measure89 = new Measure(15);
        Measure measure90 = new Measure(15);
        Measure measure91 = new Measure(16);
        Measure measure92 = new Measure(16);
        Measure measure93 = new Measure(17);
        Measure measure94 = new Measure(18);
        Measure measure95 = new Measure(20);
        Measure measure96 = new Measure(21);
        Measure measure97 = new Measure(23);
        Measure measure98 = new Measure(26);
        Measure measure99 = new Measure(30);
        Measure measure100 = new Measure(34);
        Measure measure101 = new Measure(38);
        Measure measure102 = new Measure(42);
        Measure measure103 = new Measure(46);
        Measure measure104 = new Measure(50);
        Measure measure105 = new Measure(54);
        Measure measure106 = new Measure(58);
        Measure measure107 = new Measure(62);
        Measure measure108 = new Measure(66);
        Measure measure109 = new Measure(70);
        Measure measure110 = new Measure(73);
        Measure measure111 = new Measure(76);
        Measure measure112 = new Measure(79);
        Measure measure113 = new Measure(83);
        Measure measure114 = new Measure(85);
        Measure measure115 = new Measure(87);
        Measure measure116 = new Measure(89);
        Measure measure117 = new Measure(91);
        Measure measure118 = new Measure(92);
        Measure measure119 = new Measure(93);
        Measure measure120 = new Measure(95);
        Measure measure121 = new Measure(96);
        Measure measure122 = new Measure(97);
        Measure measure123 = new Measure(98);
        Measure measure124 = new Measure(99);
        Measure measure125 = new Measure(100);
        Measure measure126 = new Measure(101);
        Measure measure127 = new Measure(102);
        Measure measure128 = new Measure(103);
        Measure measure129 = new Measure(104);
        Measure measure130 = new Measure(104);
        Measure measure131 = new Measure(103);
        Measure measure132 = new Measure(102);
        Measure measure133 = new Measure(101);
        Measure measure134 = new Measure(100);
        Measure measure135 = new Measure(99);
        Measure measure136 = new Measure(98);
        Measure measure137 = new Measure(99);
        Measure measure138 = new Measure(100);
        Measure measure139 = new Measure(100);
        Measure measure140 = new Measure(99);
        Measure measure141 = new Measure(100);
        Measure measure142 = new Measure(100);
        Measure measure143 = new Measure(100);
        Measure measure144 = new Measure(100);
        Measure measure145 = new Measure(100);
        Measure measure146 = new Measure(100);
        Measure measure147 = new Measure(100);
        Measure measure148 = new Measure(101);
        Measure measure149 = new Measure(100);
        Measure measure150 = new Measure(100);
        Measure measure151 = new Measure(100);
        Measure measure152 = new Measure(100);
        Measure measure153 = new Measure(99);
        Measure measure154 = new Measure(99);
        Measure measure155 = new Measure(100);
        Measure measure156 = new Measure(101);
        Measure measure157 = new Measure(100);
        Measure measure158 = new Measure(101);
        Measure measure159 = new Measure(100);
        Measure measure160 = new Measure(100);
        Measure measure161 = new Measure(102);
        Measure measure162 = new Measure(101);
        Measure measure163 = new Measure(100);
        Measure measure164 = new Measure(99);
        Measure measure165 = new Measure(100);
        Measure measure166 = new Measure(100);
        Measure measure167 = new Measure(100);
        Measure measure168 = new Measure(100);
        Measure measure169 = new Measure(99);
        Measure measure170 = new Measure(100);
        Measure measure171 = new Measure(100);
        Measure measure172 = new Measure(100);
        Measure measure173 = new Measure(99);
        Measure measure174 = new Measure(100);
        Measure measure175 = new Measure(101);
        Measure measure176 = new Measure(100);
        Measure measure177 = new Measure(100);
        Measure measure178 = new Measure(101);
        Measure measure179 = new Measure(100);
        Measure measure180 = new Measure(100);
        Measure measure181 = new Measure(100);
        Measure measure182 = new Measure(100);
        Measure measure183 = new Measure(98);
        Measure measure184 = new Measure(99);
        Measure measure185 = new Measure(100);
        Measure measure186 = new Measure(101);
        Measure measure187 = new Measure(100);
        Measure measure188 = new Measure(101);
        Measure measure189 = new Measure(100);
        Measure measure190 = new Measure(99);
        Measure measure191 = new Measure(98);
        Measure measure192 = new Measure(100);
        Measure measure193 = new Measure(101);
        Measure measure194 = new Measure(101);
        Measure measure195 = new Measure(100);
        Measure measure196 = new Measure(100);
        Measure measure197 = new Measure(100);
        Measure measure198 = new Measure(99);
        Measure measure199 = new Measure(100);
        Measure measure200 = new Measure(100);
        Measure measure201 = new Measure(99);
        Measure measure202 = new Measure(98);
        Measure measure203 = new Measure(97);
        Measure measure204 = new Measure(96);
        Measure measure205 = new Measure(95);
        Measure measure206 = new Measure(93);
        Measure measure207 = new Measure(91);
        Measure measure208 = new Measure(90);
        Measure measure209 = new Measure(85);
        Measure measure210 = new Measure(82);
        Measure measure211 = new Measure(76);
        Measure measure212 = new Measure(70);
        Measure measure213 = new Measure(65);
        Measure measure214 = new Measure(60);
        Measure measure215 = new Measure(55);
        Measure measure216 = new Measure(55);
        Measure measure217 = new Measure(45);
        Measure measure218 = new Measure(35);
        Measure measure219 = new Measure(30);
        Measure measure220 = new Measure(25);
        Measure measure221 = new Measure(23);
        Measure measure222 = new Measure(21);
        Measure measure223 = new Measure(19);
        Measure measure224 = new Measure(18);
        Measure measure225 = new Measure(17);
        Measure measure226 = new Measure(16);
        Measure measure227 = new Measure(15);
        Measure measure228 = new Measure(15);
        Measure measure229 = new Measure(15);
        Measure measure230 = new Measure(14);
        Measure measure231 = new Measure(14);
        Measure measure232 = new Measure(14);
        Measure measure233 = new Measure(14);
        Measure measure234 = new Measure(13);
        Measure measure235 = new Measure(13);
        Measure measure236 = new Measure(12);
        Measure measure237 = new Measure(12);
        Measure measure238 = new Measure(13);
        Measure measure239 = new Measure(14);
        Measure measure240 = new Measure(15);
        Measure measure241 = new Measure(15);
        Measure measure242 = new Measure(16);
        Measure measure243 = new Measure(16);
        Measure measure244 = new Measure(17);
        Measure measure245 = new Measure(19);
        Measure measure246 = new Measure(21);
        Measure measure247 = new Measure(23);
        Measure measure248 = new Measure(26);
        Measure measure249 = new Measure(30);
        Measure measure250 = new Measure(33);
        Measure measure251 = new Measure(36);
        Measure measure252 = new Measure(41);
        Measure measure253 = new Measure(46);
        Measure measure254 = new Measure(50);
        Measure measure255 = new Measure(54);
        Measure measure256 = new Measure(58);
        Measure measure257 = new Measure(62);
        Measure measure258 = new Measure(66);
        Measure measure259 = new Measure(70);
        Measure measure260 = new Measure(73);
        Measure measure261 = new Measure(76);
        Measure measure262 = new Measure(79);
        Measure measure263 = new Measure(83);
        Measure measure264 = new Measure(85);
        Measure measure265 = new Measure(87);
        Measure measure266 = new Measure(89);
        Measure measure267 = new Measure(91);
        Measure measure268 = new Measure(93);
        Measure measure269 = new Measure(93);
        Measure measure270 = new Measure(94);
        Measure measure271 = new Measure(95);
        Measure measure272 = new Measure(96);
        Measure measure273 = new Measure(98);
        Measure measure274 = new Measure(99);
        Measure measure275 = new Measure(100);
        Measure measure276 = new Measure(101);
        Measure measure277 = new Measure(102);
        Measure measure278 = new Measure(103);
        Measure measure279 = new Measure(104);
        Measure measure280 = new Measure(104);
        Measure measure281 = new Measure(103);
        Measure measure282 = new Measure(102);
        Measure measure283 = new Measure(101);
        Measure measure284 = new Measure(100);
        Measure measure285 = new Measure(99);
        Measure measure286 = new Measure(98);
        Measure measure287 = new Measure(98);
        Measure measure288 = new Measure(99);
        Measure measure289 = new Measure(100);
        Measure measure290 = new Measure(101);
        Measure measure291 = new Measure(100);
        Measure measure292 = new Measure(100);
        Measure measure293 = new Measure(100);
        Measure measure294 = new Measure(100);
        Measure measure295 = new Measure(100);
        Measure measure296 = new Measure(100);
        Measure measure297 = new Measure(100);
        Measure measure298 = new Measure(101);
        Measure measure299 = new Measure(100);
        Measure measure300 = new Measure((int)((2199-315)/CONV_FACTOR));

        MeasuresList.add(measure1);
        MeasuresList.add(measure2);
        MeasuresList.add(measure3);
        MeasuresList.add(measure4);
        MeasuresList.add(measure5);
        MeasuresList.add(measure6);
        MeasuresList.add(measure7);
        MeasuresList.add(measure8);
        MeasuresList.add(measure9);
        MeasuresList.add(measure10);
        MeasuresList.add(measure11);
        MeasuresList.add(measure12);
        MeasuresList.add(measure13);
        MeasuresList.add(measure14);
        MeasuresList.add(measure15);
        MeasuresList.add(measure16);
        MeasuresList.add(measure17);
        MeasuresList.add(measure18);
        MeasuresList.add(measure19);
        MeasuresList.add(measure20);
        MeasuresList.add(measure21);
        MeasuresList.add(measure22);
        MeasuresList.add(measure23);
        MeasuresList.add(measure24);
        MeasuresList.add(measure25);
        MeasuresList.add(measure26);
        MeasuresList.add(measure27);
        MeasuresList.add(measure28);
        MeasuresList.add(measure29);
        MeasuresList.add(measure30);
        MeasuresList.add(measure31);
        MeasuresList.add(measure32);
        MeasuresList.add(measure33);
        MeasuresList.add(measure34);
        MeasuresList.add(measure35);
        MeasuresList.add(measure36);
        MeasuresList.add(measure37);
        MeasuresList.add(measure38);
        MeasuresList.add(measure39);
        MeasuresList.add(measure40);
        MeasuresList.add(measure41);
        MeasuresList.add(measure42);
        MeasuresList.add(measure43);
        MeasuresList.add(measure44);
        MeasuresList.add(measure45);
        MeasuresList.add(measure46);
        MeasuresList.add(measure47);
        MeasuresList.add(measure48);
        MeasuresList.add(measure49);
        MeasuresList.add(measure50);
        MeasuresList.add(measure51);
        MeasuresList.add(measure52);
        MeasuresList.add(measure53);
        MeasuresList.add(measure54);
        MeasuresList.add(measure55);
        MeasuresList.add(measure56);
        MeasuresList.add(measure57);
        MeasuresList.add(measure58);
        MeasuresList.add(measure59);
        MeasuresList.add(measure60);
        MeasuresList.add(measure61);
        MeasuresList.add(measure62);
        MeasuresList.add(measure63);
        MeasuresList.add(measure64);
        MeasuresList.add(measure65);
        MeasuresList.add(measure66);
        MeasuresList.add(measure67);
        MeasuresList.add(measure68);
        MeasuresList.add(measure69);
        MeasuresList.add(measure70);
        MeasuresList.add(measure71);
        MeasuresList.add(measure72);
        MeasuresList.add(measure73);
        MeasuresList.add(measure74);
        MeasuresList.add(measure75);
        MeasuresList.add(measure76);
        MeasuresList.add(measure77);
        MeasuresList.add(measure78);
        MeasuresList.add(measure79);
        MeasuresList.add(measure80);
        MeasuresList.add(measure81);
        MeasuresList.add(measure82);
        MeasuresList.add(measure83);
        MeasuresList.add(measure84);
        MeasuresList.add(measure85);
        MeasuresList.add(measure86);
        MeasuresList.add(measure87);
        MeasuresList.add(measure88);
        MeasuresList.add(measure89);
        MeasuresList.add(measure90);
        MeasuresList.add(measure91);
        MeasuresList.add(measure92);
        MeasuresList.add(measure93);
        MeasuresList.add(measure94);
        MeasuresList.add(measure95);
        MeasuresList.add(measure96);
        MeasuresList.add(measure97);
        MeasuresList.add(measure98);
        MeasuresList.add(measure99);
        MeasuresList.add(measure100);
        MeasuresList.add(measure101);
        MeasuresList.add(measure102);
        MeasuresList.add(measure103);
        MeasuresList.add(measure104);
        MeasuresList.add(measure105);
        MeasuresList.add(measure106);
        MeasuresList.add(measure107);
        MeasuresList.add(measure108);
        MeasuresList.add(measure109);
        MeasuresList.add(measure110);
        MeasuresList.add(measure111);
        MeasuresList.add(measure112);
        MeasuresList.add(measure113);
        MeasuresList.add(measure114);
        MeasuresList.add(measure115);
        MeasuresList.add(measure116);
        MeasuresList.add(measure117);
        MeasuresList.add(measure118);
        MeasuresList.add(measure119);
        MeasuresList.add(measure120);
        MeasuresList.add(measure121);
        MeasuresList.add(measure122);
        MeasuresList.add(measure123);
        MeasuresList.add(measure124);
        MeasuresList.add(measure125);
        MeasuresList.add(measure126);
        MeasuresList.add(measure127);
        MeasuresList.add(measure128);
        MeasuresList.add(measure129);
        MeasuresList.add(measure130);
        MeasuresList.add(measure131);
        MeasuresList.add(measure132);
        MeasuresList.add(measure133);
        MeasuresList.add(measure134);
        MeasuresList.add(measure135);
        MeasuresList.add(measure136);
        MeasuresList.add(measure137);
        MeasuresList.add(measure138);
        MeasuresList.add(measure139);
        MeasuresList.add(measure140);
        MeasuresList.add(measure141);
        MeasuresList.add(measure142);
        MeasuresList.add(measure143);
        MeasuresList.add(measure144);
        MeasuresList.add(measure145);
        MeasuresList.add(measure146);
        MeasuresList.add(measure147);
        MeasuresList.add(measure148);
        MeasuresList.add(measure149);
        MeasuresList.add(measure150);
        MeasuresList.add(measure151);
        MeasuresList.add(measure152);
        MeasuresList.add(measure153);
        MeasuresList.add(measure154);
        MeasuresList.add(measure155);
        MeasuresList.add(measure156);
        MeasuresList.add(measure157);
        MeasuresList.add(measure158);
        MeasuresList.add(measure159);
        MeasuresList.add(measure160);
        MeasuresList.add(measure161);
        MeasuresList.add(measure162);
        MeasuresList.add(measure163);
        MeasuresList.add(measure164);
        MeasuresList.add(measure165);
        MeasuresList.add(measure166);
        MeasuresList.add(measure167);
        MeasuresList.add(measure168);
        MeasuresList.add(measure169);
        MeasuresList.add(measure170);
        MeasuresList.add(measure171);
        MeasuresList.add(measure172);
        MeasuresList.add(measure173);
        MeasuresList.add(measure174);
        MeasuresList.add(measure175);
        MeasuresList.add(measure176);
        MeasuresList.add(measure177);
        MeasuresList.add(measure178);
        MeasuresList.add(measure179);
        MeasuresList.add(measure180);
        MeasuresList.add(measure181);
        MeasuresList.add(measure182);
        MeasuresList.add(measure183);
        MeasuresList.add(measure184);
        MeasuresList.add(measure185);
        MeasuresList.add(measure186);
        MeasuresList.add(measure187);
        MeasuresList.add(measure188);
        MeasuresList.add(measure189);
        MeasuresList.add(measure190);
        MeasuresList.add(measure191);
        MeasuresList.add(measure192);
        MeasuresList.add(measure193);
        MeasuresList.add(measure194);
        MeasuresList.add(measure195);
        MeasuresList.add(measure196);
        MeasuresList.add(measure197);
        MeasuresList.add(measure198);
        MeasuresList.add(measure199);
        MeasuresList.add(measure200);
        MeasuresList.add(measure201);
        MeasuresList.add(measure202);
        MeasuresList.add(measure203);
        MeasuresList.add(measure204);
        MeasuresList.add(measure205);
        MeasuresList.add(measure206);
        MeasuresList.add(measure207);
        MeasuresList.add(measure208);
        MeasuresList.add(measure209);
        MeasuresList.add(measure210);
        MeasuresList.add(measure211);
        MeasuresList.add(measure212);
        MeasuresList.add(measure213);
        MeasuresList.add(measure214);
        MeasuresList.add(measure215);
        MeasuresList.add(measure216);
        MeasuresList.add(measure217);
        MeasuresList.add(measure218);
        MeasuresList.add(measure219);
        MeasuresList.add(measure220);
        MeasuresList.add(measure221);
        MeasuresList.add(measure222);
        MeasuresList.add(measure223);
        MeasuresList.add(measure224);
        MeasuresList.add(measure225);
        MeasuresList.add(measure226);
        MeasuresList.add(measure227);
        MeasuresList.add(measure228);
        MeasuresList.add(measure229);
        MeasuresList.add(measure230);
        MeasuresList.add(measure231);
        MeasuresList.add(measure232);
        MeasuresList.add(measure233);
        MeasuresList.add(measure234);
        MeasuresList.add(measure235);
        MeasuresList.add(measure236);
        MeasuresList.add(measure237);
        MeasuresList.add(measure238);
        MeasuresList.add(measure239);
        MeasuresList.add(measure240);
        MeasuresList.add(measure241);
        MeasuresList.add(measure242);
        MeasuresList.add(measure243);
        MeasuresList.add(measure244);
        MeasuresList.add(measure245);
        MeasuresList.add(measure246);
        MeasuresList.add(measure247);
        MeasuresList.add(measure248);
        MeasuresList.add(measure249);
        MeasuresList.add(measure250);
        MeasuresList.add(measure251);
        MeasuresList.add(measure252);
        MeasuresList.add(measure253);
        MeasuresList.add(measure254);
        MeasuresList.add(measure255);
        MeasuresList.add(measure256);
        MeasuresList.add(measure257);
        MeasuresList.add(measure258);
        MeasuresList.add(measure259);
        MeasuresList.add(measure260);
        MeasuresList.add(measure261);
        MeasuresList.add(measure262);
        MeasuresList.add(measure263);
        MeasuresList.add(measure264);
        MeasuresList.add(measure265);
        MeasuresList.add(measure266);
        MeasuresList.add(measure267);
        MeasuresList.add(measure268);
        MeasuresList.add(measure269);
        MeasuresList.add(measure270);
        MeasuresList.add(measure271);
        MeasuresList.add(measure272);
        MeasuresList.add(measure273);
        MeasuresList.add(measure274);
        MeasuresList.add(measure275);
        MeasuresList.add(measure276);
        MeasuresList.add(measure277);
        MeasuresList.add(measure278);
        MeasuresList.add(measure279);
        MeasuresList.add(measure280);
        MeasuresList.add(measure281);
        MeasuresList.add(measure282);
        MeasuresList.add(measure283);
        MeasuresList.add(measure284);
        MeasuresList.add(measure285);
        MeasuresList.add(measure286);
        MeasuresList.add(measure287);
        MeasuresList.add(measure288);
        MeasuresList.add(measure289);
        MeasuresList.add(measure290);
        MeasuresList.add(measure291);
        MeasuresList.add(measure292);
        MeasuresList.add(measure293);
        MeasuresList.add(measure294);
        MeasuresList.add(measure295);
        MeasuresList.add(measure296);
        MeasuresList.add(measure297);
        MeasuresList.add(measure298);
        MeasuresList.add(measure299);
        MeasuresList.add(measure300);
    }

    private void createBackSerie() {
        arcView.addSeries(new SeriesItem.Builder(Color.WHITE)
                .setRange(minValue, maxValue, maxValue)
                .setInitialVisibility(false)
                .setLineWidth(10f)
                .setDrawAsPoint(false)
                .build());

        arcView.configureAngles(280, 0);
    }

    private void createDataSerie1() {
        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(minValue, maxValue, minValue)
                .setLineWidth(6f)
                .build();

        serie1Index = arcView.addSeries(seriesItem1);

    }


    private void createEvents() {
        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(0)
                .setDuration(0)
                .build());

        arcView.addEvent(new DecoEvent.Builder(data).setIndex(serie1Index).setDelay(0).build());
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        return intentFilter;
    }


    //ObservableArrayList for the Decoder

}
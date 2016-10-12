package edu.wisc.ece.stepcounter;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    TextView txtCounter;
    TextView textx, texty, textz;

    boolean isStep;
    int count;
    float max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textx = (TextView) findViewById(R.id.xval);
        texty = (TextView) findViewById(R.id.yval);
        textz = (TextView) findViewById(R.id.zval);

        txtCounter = (TextView) findViewById(R.id.textView);
        //view = findViewById(R.id.textView);

        count = 0;
        max = 0;
        isStep = false;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            // Do your job here with accelerometer data in event
            displayAccelerometer(event);
        }
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            displayOrientation(event);
        }
    }

    private void displayAccelerometer(SensorEvent event) {
        // Accelerometer sensor returns 3 values, one for each axis.
        // Your code here:
        final float x = event.values[0];
        final float y = event.values[1];
        final float z = event.values[2];

        final float measure = x*x + y*y + z*z;

        if(measure>max){
            max = measure;
        }

        if(measure>7) {
            if (!isStep) {
                count++;
                isStep = true;
            }
        }else if(measure<3){
            isStep = false;
        }

        txtCounter.post(new Runnable(){
            @Override
            public void run(){
                txtCounter.setText("Steps: " + count);
            }
        });

        textx.post(new Runnable(){
            @Override
            public void run(){
                textx.setText("Measure: " + measure);
            }
        });

        texty.post(new Runnable(){
            @Override
            public void run(){
                texty.setText("Max: " + max);
            }
        });
    }

    private void displayOrientation(SensorEvent event){
        final float angle = event.values[0];

        if(angle<22.5 || angle>=337.5){
            textz.post(new Runnable(){
                @Override
                public void run(){
                    textz.setText("Direction: N");
                }
            });
        }
        else if(angle>22.5 && angle<=67.5){
            textz.post(new Runnable(){
                @Override
                public void run(){
                    textz.setText("Direction: NE");
                }
            });
        }
        else if(angle>67.5 && angle<=112.5){
            textz.post(new Runnable(){
                @Override
                public void run(){
                    textz.setText("Direction: E");
                }
            });
        }
        else if(angle>112.5 && angle<=157.5){
            textz.post(new Runnable(){
                @Override
                public void run(){
                    textz.setText("Direction: SE");
                }
            });
        }
        else if(angle>157.5 && angle<=202.5){
            textz.post(new Runnable(){
                @Override
                public void run(){
                    textz.setText("Direction: S");
                }
            });
        }
        else if(angle>202.5 && angle<=247.5){
            textz.post(new Runnable(){
                @Override
                public void run(){
                    textz.setText("Direction: SW");
                }
            });
        }
        else if(angle>247.5 && angle<=292.5){
            textz.post(new Runnable(){
                @Override
                public void run(){
                    textz.setText("Direction: W");
                }
            });
        }
        else{
            textz.post(new Runnable(){
                @Override
                public void run(){
                    textz.setText("Direction: NW");
                }
            });
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}

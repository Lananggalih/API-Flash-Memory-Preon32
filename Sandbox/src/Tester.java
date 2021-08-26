import com.virtenio.driver.device.ADXL345;
import com.virtenio.driver.device.MPL115A2;
import com.virtenio.driver.device.SHT21;
import com.virtenio.driver.gpio.GPIO;
import com.virtenio.driver.gpio.NativeGPIO;
import com.virtenio.driver.i2c.I2C;
import com.virtenio.driver.i2c.NativeI2C;
import com.virtenio.driver.spi.NativeSPI;

public class Tester {
	
	public static void main(String[] args) throws Exception
	{

		Tabel tabel = new Tabel();
		ModuleSave ms = new ModuleSave(tabel);
		ms.flashErase();
		
		NativeI2C i2c;
		SHT21 sht21;
		
		
		i2c = NativeI2C.getInstance(1);
		i2c.open(I2C.DATA_RATE_400);

		sht21 = new SHT21(i2c);
		sht21.open();
		sht21.setResolution(SHT21.RESOLUTION_RH12_T14);
		sht21.reset();

		
		
		
		ADXL345 accelerationSensor;
		GPIO accelIrqPin1;
		GPIO accelIrqPin2;
		GPIO accelCs;
		
		accelIrqPin1 = NativeGPIO.getInstance(37);
		accelCs = NativeGPIO.getInstance(20);

		NativeSPI spi = NativeSPI.getInstance(0);
		spi.open(ADXL345.SPI_MODE, ADXL345.SPI_BIT_ORDER, ADXL345.SPI_MAX_SPEED);

		accelerationSensor = new ADXL345(spi, accelCs);
		accelerationSensor.open();
		accelerationSensor.setDataFormat(ADXL345.DATA_FORMAT_RANGE_2G);
		accelerationSensor.setDataRate(ADXL345.DATA_RATE_3200HZ);
		accelerationSensor.setPowerControl(ADXL345.POWER_CONTROL_MEASURE);
		
		MPL115A2 pressureSensor;
		
		System.out.println("GPIO(Init)");
		GPIO resetPin = NativeGPIO.getInstance(24);
		GPIO shutDownPin = NativeGPIO.getInstance(12);
		
		
		

		System.out.println("MPL115A2(Init)");
		pressureSensor = new MPL115A2(i2c, resetPin, shutDownPin);
		pressureSensor.open();
		pressureSensor.setReset(false);
		pressureSensor.setShutdown(false);
		
		
		
		while (true) {
			try {
				// temperature conversion
				sht21.startTemperatureConversion();
				Thread.sleep(100);
				int rawT = sht21.getTemperatureRaw();
				for(int i=0;i<5;i++)
				{
				float t=SHT21.convertRawTemperatureToCelsius(rawT);
				String data = Float.toString(t);
				ms.writeFlash("T".getBytes());
				ms.writeFlash(ms.convert(data));
				}
				
				//humidity conversion
				Thread.sleep(100);
				sht21.startRelativeHumidityConversion();
				Thread.sleep(100);
				int rawRH = sht21.getRelativeHumidityRaw();
				for(int i=0;i<5;i++) {
				float rh = SHT21.convertRawRHToRHw(rawRH);
				String data2 = Float.toString(rh);
				ms.writeFlash("H".getBytes());
				ms.writeFlash(ms.convert(data2));
				}
				
				//accel conversion
				Thread.sleep(100);
				short[] values = new short[3];
				for(int i=0;i<5;i++)
				{
				accelerationSensor.getValuesRaw(values, 0);
				String x=Short.toString(values[0]);
				String y=Short.toString(values[1]);
				String z=Short.toString(values[2]);
				ms.writeFlash("A".getBytes());
				ms.writeFlash(x.getBytes());
				ms.writeFlash(y.getBytes());
				ms.writeFlash(z.getBytes());
				}
				
				//pressure conversion
				pressureSensor.startBothConversion();
				Thread.sleep(MPL115A2.BOTH_CONVERSION_TIME);
				int pressurePr = pressureSensor.getPressureRaw();
				int tempRaw = pressureSensor.getTemperatureRaw();
				for(int i=0;i<5;i++)
				{
				float pressure = pressureSensor.compensate(pressurePr, tempRaw);
				String data3 = Float.toString(pressure);
				ms.writeFlash("P".getBytes());
				ms.writeFlash(ms.convert(data3));
				}
				
				String[] s = ms.flashRead();
				boolean status=tabel.tabelPenuh();
				
				
				float temperature=0;
				int counterTemperature=0;
				
				float humidity=0;
				int counterHumidity=0;
				
				
				short x1=0;
				short y1=0;
				short z1=0;
				int counterAccel=0;
				
				float pressure=0;
				int counterPressure=0;
				
				if(status==true)
				{
					break;
				}
				
				
				int i=0;
				while(i<s.length)
				{	
					
					if(s[i]==null)
					{
						break;
					}
					
					if(s[i].equals("T"))
					{
						temperature+=Float.parseFloat(s[i+1]);
						counterTemperature+=1;
					}
					
					if(s[i].equals("H"))
					{
						humidity+=Float.parseFloat(s[i+1]);
						counterHumidity+=1;
					}
					
					if(s[i].equals("A"))
					{
						x1+=Short.valueOf(s[i+1]);
						y1+=Short.valueOf(s[i+2]);
						z1+=Short.valueOf(s[i+3]);
						counterAccel+=1;
					}
					
					if(s[i].equals("P"))
					{
						pressure+=Float.parseFloat(s[i+1]);
						counterPressure+=x1;
					}
					
					i++;					
				}
				
				System.out.println();
				System.out.println();
				float rataTemperature=temperature/counterTemperature;
				float rataHumidity = humidity/counterHumidity;
				short rataX=(short)(x1/counterAccel);
				short rataY=(short)(y1/counterAccel);
				short rataZ=(short)(z1/counterAccel);
				float rataPressure=pressure/counterPressure;
				System.out.println("Rata-Rata Temperature = "+rataTemperature);
				System.out.println("Rata-Rata Humidity = "+rataHumidity);
				System.out.println("Rata-Rata Accel = "+rataX+" , "+rataY+" , "+rataZ);
				System.out.println("Rata-Rata Pressure = "+rataPressure);
				System.out.println();
				System.out.println();
				ms.flashSpace();
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("SHT21 error");
			}
					
		}
		
		
		
		
		
	}
			
	}

import com.virtenio.driver.device.SHT21;
import com.virtenio.driver.i2c.I2C;
import com.virtenio.driver.i2c.NativeI2C;

public class Tester2 {
	
	public static void main(String[] args)throws Exception
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
		
		while (true) {
			try {
				// temperature conversion
				sht21.startTemperatureConversion();
				Thread.sleep(100);
				int rawT = sht21.getTemperatureRaw();
				for(int i=0;i<10;i++)
				{
				float t=SHT21.convertRawTemperatureToCelsius(rawT);
				String data = Float.toString(t);
				ms.writeFlash(ms.convert(data));
				}
				System.out.println();
				System.out.println(ms.flashSpace());
			}catch (Exception e) {
				System.out.println("SHT21 error");
			}
		
	}

	}
}

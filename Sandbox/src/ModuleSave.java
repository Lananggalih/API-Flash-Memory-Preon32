import com.virtenio.driver.flash.Flash;
import com.virtenio.preon32.node.Node;


public class ModuleSave {
	
	int alamatAwal; //atribut ini digunakan untuk menyimpan alamat sekarang
	int size ;//atribut ini digunakan untuk menyimpan ukuran dari flash memory
	Tabel tabel;//atribut ini digunakan untuk mengakses class tabel
	
	public ModuleSave(Tabel tabel)
	{
		this.tabel=tabel;
		this.size=1048576;
		this.alamatAwal=0;
	}
	
	//Method ini digunakan untuk menyimpan data dan sekaligus mencatat ke dalam table(ini API)
	public void writeFlash(byte[] data) throws Exception
	{
		
		Flash flash =Node.getInstance().getFlash();
		flash.open();
		int counter=0;//atribut untuk iterasi
		String hasilData="";//atribut untuk menyimpan hasil simpan
		
		while(counter<=data.length)
		{
			if(counter==data.length)
			{
				break;
			}
			
			if(this.size<data[counter])
			{
				System.out.println("Memory Penuh");
				break;
			}
			
			if(this.size>=data[counter])
						{
							flash.write(this.alamatAwal, data[counter]);
							flash.waitWhileBusy();
							int readflash = flash.read(this.alamatAwal);
							byte[] b = new byte[1];//atribut menampung byte
							b[0]=(byte)readflash;
							String s = new String(b);//melakukan encode untuk mengembalikan dalam bentuk String
							this.size-=data[counter];
							this.alamatAwal=this.alamatAwal+data[counter];
							hasilData=hasilData+s;
						}		
		 counter+=1;
		}
		
		tabel.updateTabel(this.alamatAwal, hasilData);//memanggil method di tabel untuk mengisi tabel
		flash.close();	
		}
		
	
	//method ini digunakan untuk mengambil hasil dari atribut hasil
	public int getalamatAwal()
	{
		return this.alamatAwal;
	}
	
		
	
	//method ini digunakan untuk menghapus semua isi flash memory dan mengembalikan atribut seperti semula di set(ini API)
	public void flashErase() throws Exception
	{	
		
			Flash flash = Node.getInstance().getFlash();
			flash.open();
			flash.eraseChip();
			tabel.emptyTabel();
			this.alamatAwal=0;
			this.size=1048576;
			System.out.println("berhasil menghapus semua isi Flash Memory");
			flash.close();
		
	}
	
	
	//method ini digunakan untuk mengembalikan ukuran flash memory yang sekarang
	public int flashSpace() throws Exception
	{
			return this.size;		
	}
	
	
	//method ini digunakan untuk membaca apa saja yang sudah ada di dalam flash memory sekarang(ini API)
	public String[] flashRead() throws Exception
	{
		String[][] tabel = this.tabel.getTabel();//mengambil tabel
		String [] hasil = new String[101];//atribut ini digunakan untuk menyimpan data dari kolom data di tabel
		for(int i=0;i<tabel.length;i++)
		{
			
			if(tabel[i][1]==null)
			{
				break;
			}
			else
			{
				hasil[i]=tabel[i][1];
			}
			
			
		}
		return hasil;
	}	
	
	
	//method ini digunakan untuk merubah data String ke dalam type data byte
	public byte[] convert(String s)
	{
		byte[] hasil=s.getBytes();
		return hasil;
	}
	
	
		
	
	
}

	
	


public class Tabel {
	
        String[][] tabel;//atribut array dua dimensi untuk membuat tabel
        
	
       public Tabel()
        {
        	this.tabel =new String[101][2];    
        }
	
	
	
        //method ini digunakan untuk mengisi tabel
        public void updateTabel(int address,String data)
        {
            for(int i=0;i<tabel.length;i++)
            {
                if(tabel[i][0]==null)
                {
                	tabel[i][0]=Integer.toHexString(address)+" ";
                    tabel[i][1]=data;
                    break;
                    
                }
                
            }
        }
        
        //method untuk mendapatkan tabel
        public String[][] getTabel()
        {
        	return this.tabel;
        }
        
      
    //method ini di gunakan untuk mengkosong kan tabel   
    public void emptyTabel()
    {
    	for (int i = 0; i < tabel.length; i++) {
		{
			if(tabel[i][0]!=null)
			{
				tabel[i][0]=null;
				tabel[i][1]=null;
			}
			else
			{
				break;
			}
			
		}
		}
    }
    
    //method ini digunakan untuk memberitahu apakah sudah penuh atau 
      public boolean tabelPenuh()
      {
    	  boolean hasil = false;
    	  int counter =0;
    	  for(int i=0;i<tabel.length;i++)
    	  {
    		  if(tabel[i][0]!=null)
    		  {
    			  counter+=1;
    		  }
    		  
    		  if(counter==tabel.length)
    		  {
    			  hasil=true;
    		  }
    		  
    	  }
    	  return hasil;
      }
        
    //method ini digunakan untuk menampilkan table
	public void printTable()
	{
		for (int i = 0; i < tabel.length; i++) {
			System.out.println(" ");
			for(int j=0;j<tabel[i].length;j++)
		{
				if(tabel[i][j]==null)
				{
					break;
				}
				System.out.print(tabel[i][j]);
			
		}
		}
	}
		}




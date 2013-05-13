package jaxb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import user.Userdb;


public class Unmarshal {
	public void setUnmarshal(String path) throws IOException, JAXBException {
			JAXBContext jc = JAXBContext.newInstance(Userdb.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			InputStream inputStream = new FileInputStream(path);
			Reader reader = new InputStreamReader(inputStream,"UTF-8");
		    Userdb userdb = new Userdb();
		    try {
		    	userdb = (Userdb) unmarshaller.unmarshal(reader);
		    } finally {
		    	reader.close();
		    }
		
	}
}

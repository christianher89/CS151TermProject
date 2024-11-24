package application;

import java.io.IOException;
import java.util.List;

public interface Editable {
	
	public List<String> getEditableFields();
	
	public void save() throws IOException;
}

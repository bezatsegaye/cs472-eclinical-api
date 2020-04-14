package repository.desease;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dao.Database;
import model.desease.Desease;
import model.desease.DeseaseCategory;

public class DeseaseRepository {
	private static DeseaseRepository deseaseRepository;
	private Database database = Database.getInstance();
	private DeseaseRepository() {}
	public static DeseaseRepository getInstance() {
		if(deseaseRepository == null)
			deseaseRepository = new DeseaseRepository();
		return deseaseRepository;
	}
	public boolean saveDesease(Desease desease) {
		boolean isSuccess = false;
		try {
			if(desease != null) 
				isSuccess = database.executeStatement("INSERT into desease(deseasename, desease_category_id) VALUES(?, ?)", 
													  Arrays.asList(desease.getDeseaseName(), desease.getdeseaseCategory().getDeseaseTypeId()));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return isSuccess;
	}
	public List<Desease> loadDeseases() {
		List<Desease> deseases = new ArrayList<>();
		try {
			ResultSet deseaseResult = database.getResult("SELECT * FROM desease", null);
			while(deseaseResult.next()) {
				DeseaseCategory deseaseCategory = new DeseaseCategory();
				ResultSet deseaseCategoryResult = database.getResult("SELECT * FROM desease_category WHERE deseasecategoryid = ?", Arrays.asList(deseaseResult.getInt("desease_category_id")));
				if(deseaseCategoryResult.next()) 
					deseaseCategory = new DeseaseCategory(deseaseCategoryResult.getInt(1), deseaseCategoryResult.getString(2));
				deseases.add(new Desease(deseaseResult.getInt(1), deseaseResult.getString(2), deseaseCategory));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return deseases;
	}
	public boolean deleteDeseaseById(int deseaseId) {
		boolean isSuccess = false;
		try {
			isSuccess = database.executeStatement("DELETE FROM desease where deseaseid = ?", Arrays.asList(deseaseId));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return isSuccess;
	}
	public boolean updateDeseaseById(Desease desease) {
		boolean isSuccess = false;
		try {
			isSuccess = database.executeStatement("UPDATE desease SET deseasename = ?, desease_category_id = ? WHERE deseaseid = ?", 
												   Arrays.asList(desease.getDeseaseName(), 
																 desease.getdeseaseCategory().getDeseaseTypeId(),
																 desease.getDeseaseId()));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return isSuccess;
	}
}

package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mechalikh.pureedgesim.MainApplication;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;

public class PaperLog {
	private HashMap<String,List<String>> resultsList;

	public PaperLog() {	
		resultsList = new HashMap<String,List<String>>();
	}

	void addNewLog(String Name, String Fields) {
		// Add the CSV file header
		ArrayList<String> Log = new ArrayList<String>();
		resultsList.put(Name,Log);
	}

	public void cleanOutputFolder(String outputFolder) throws IOException {
		// Clean the folder where the results files will be saved
		System.out.println("PaperLog- Cleaning the outputfolder...");
		Path dir = new File(outputFolder).toPath();
		deleteDirectoryRecursion(dir);
	}

	private void deleteDirectoryRecursion(Path path) throws IOException {
		if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
			try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
				for (Path entry : entries) {
					deleteDirectoryRecursion(entry);
				}
			}
		}
		try {
			Files.delete(path);
		} catch (Exception e) {
			System.out.println("PaperLog- Could not delete file/folder: " + path.toString());
		}
	}

	public void print(String Name, String line) {
		String newLine = line;
		resultsList.get(Name).add(newLine);
	}
	
	public void saveLog(String Name) {
		// writing results in csv file
		writeFile(getFileName(Name,".csv"), resultsList.get(Name));
	}

	private void writeFile(String fileName, List<String> Lines) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
			for (String str : Lines) {
				bufferedWriter.append(str);
				bufferedWriter.newLine();
			}
			Lines.clear();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFileName(String Name, String extension) {
		//String startTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		String startTime = MyMain.startTime;
		String outputFilesName = MainApplication.getOutputFolder() + "Paper-Devices-" + SimulationParameters.MAX_NUM_OF_EDGE_DEVICES;
		new File(outputFilesName).mkdirs();
		outputFilesName+="/Paper-Devices-" + SimulationParameters.MAX_NUM_OF_EDGE_DEVICES + "-" + startTime + "-" + Name;
		return outputFilesName + extension;
	}
}



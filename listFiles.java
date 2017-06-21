// Class declaration.
public class listFiles {
  // Main method.
  public static void main(String[] args) throws Exception {
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
               System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
               System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
  }
}

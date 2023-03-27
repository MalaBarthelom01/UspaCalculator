package qa.vector.ui.pages;

public class FileOperationUtilites {
    /**
     * This method will return absolute file parh from the parameter input.
     * pseudoFilePath formath: 'PROJECT_HOME:[FileName/FolderName :].*?[FileName].ext]{1}
     *
     * @param pseudoFilePath
     * @return
     */
    public String getFilePath(String pseudoFilePath) {
        String absFilePath = null;
        String projectHomeDir = System.getProperty("user.dir");
        absFilePath = projectHomeDir;
        String[] folderNames = pseudoFilePath.split(":");
        for (String name : folderNames) {
            absFilePath += System.getProperty("file.separator") + name;
        }

        return absFilePath;
    }

}

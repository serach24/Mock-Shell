package command;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import exception.InvalidArgumentSizeException;
import inputOrOutput.Input;
import inputOrOutput.StandardError;
import storage.Directory;
import storage.DirectoryStorage;

public class Load extends Command {
    /**
     * Constructs a load command with the specified arguments, also with its
     * reference to FileSystem.
     * 
     * @param arguments arguments of the command
     */
    public Load(List<String> a, List<String> r) {
        super(a, r);
    }

    /**
     * load what have save in the fileName rebuild the file system, input
     * storage, directory storage and current work directory stored in the
     * fileName
     * 
     * @return true if the fileName is loaded
     */
    public boolean execute() {
        // check number of arguments since cd only accepts 1 argument
        if (this.arguments.size() != 1) {
            System.out.println("The Load Command needs exactly 1 parameter");
            return false;
        }
        // get the file name from the input argument
        String fileName = this.arguments.get(0);
        try {
            // parse the file with the input file name
            File file = new File(fileName);

            // get the DOM Builder Factory
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            // get the DOM Builder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // load and parse the XML document
            Document document = (Document) builder.parse(file);

            // pass the all the data stored in the document to the nodeList
            NodeList fsDIR = document.getElementsByTagName("Directory");
            for (int i = 0; i < fsDIR.getLength(); i++) {
                String dirPath = fsDIR.item(i).getTextContent();
                // get the name of the new file that the user wants to create
                String[] nameArr = dirPath.split("/");
                String dirName;
                if (nameArr.length != 0) {

                    dirName = nameArr[nameArr.length - 1];

                    // get parent's path by removing the dir's name in the path
                    String parentPath = dirPath.substring(0,
                            dirPath.length() - dirName.length());

                    if (!(dirPath.equals(null) || dirPath.equals("/"))) {
                        parentPath = parentPath.substring(0,
                                parentPath.length() - 1);
                    }
                    this.fs.addDir(parentPath, dirName);
                }

            }

            NodeList fs = document.getElementsByTagName("File");
            for (int index = 0; index < fs.getLength(); index++) {
                String fileData = fs.item(index).getTextContent();
                int seperateIndex = fileData.indexOf(" ");
                String filePath = fileData.substring(0, seperateIndex);
                String fileContent = fileData.substring(seperateIndex + 1,
                        fileData.length() - 1);
                // get the name of the new file that the user wants to
                // create
                String[] nameArr1 = filePath.split("/");
                String fileName1 = nameArr1[nameArr1.length - 1];

                // get parent's path by removing the dir's name in the path
                String parentPath1 = filePath.substring(0,
                        filePath.length() - fileName1.length());
                if (!(parentPath1.equals("") || parentPath1.equals("/"))) {
                    parentPath1 = parentPath1.substring(0,
                            parentPath1.length() - 1);
                }
                this.fs.addFile(parentPath1, fileName1, fileContent);
            }

            // get the path of current work directory
            String cwdPath = document.getElementsByTagName("currentWorkingDir")
                    .item(0).getTextContent();

            if (cwdPath != "/" && cwdPath != null) {
                Directory cwd = this.fs.searchDirByPath(cwdPath);
                this.fs.setCurrWorkDir(cwd);
            }

            // recover the history input
            NodeList historyInput = document.getElementsByTagName("record");
            for (int i = 0; i < historyInput.getLength(); i++) {
                String everyInput = historyInput.item(i).getTextContent();
                Input.addInput(everyInput);
            }

            // recover the directory storage
            NodeList dsInput = document.getElementsByTagName("path");
            for (int i1 = 0; i1 < dsInput.getLength(); i1++) {
                String everyInput = dsInput.item(i1).getTextContent();
                Directory dir = this.fs.searchDirByPath(everyInput);
                DirectoryStorage.addDir(dir);
            }

        } catch (

        Exception e) {
            StandardError.displayError("The Load Command cannot "
                + "parse the file");
            return false;
        }

        return true;

    }

    @Override
    public void checkNumArgs() throws InvalidArgumentSizeException {
        // TODO Auto-generated method stub

    }

}
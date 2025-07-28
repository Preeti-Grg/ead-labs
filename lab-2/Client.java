import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        File pdfFile = new File("document.pdf");
        File wordFile = new File("report.docx");
        File excelFile = new File("data.xlsx");
        
        try {
            DocumentParser pdfParser = DocumentParserFactory.getParser(pdfFile);
            System.out.println(pdfParser.parse(pdfFile));
            
            DocumentParser wordParser = DocumentParserFactory.getParser(wordFile);
            System.out.println(wordParser.parse(wordFile));
            
            DocumentParser excelParser = DocumentParserFactory.getParser(excelFile);
            System.out.println(excelParser.parse(excelFile));
            
            // Trying to parse an unsupported format
            File unsupportedFile = new File("presentation.odp");
            DocumentParser unsupportedParser = DocumentParserFactory.getParser(unsupportedFile);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

// Interface (non-public)
interface DocumentParser {
    String parse(File file);
    boolean supportsFormat(String fileExtension);
}

// Concrete implementations (non-public)
class PdfParser implements DocumentParser {
    @Override
    public String parse(File file) {
        return "PDF content extracted";
    }
    @Override
    public boolean supportsFormat(String fileExtension) {
        return "pdf".equalsIgnoreCase(fileExtension);
    }
}

class WordParser implements DocumentParser {
    @Override
    public String parse(File file) {
        return "Word document content extracted";
    }
    @Override
    public boolean supportsFormat(String fileExtension) {
        return "docx".equalsIgnoreCase(fileExtension) || 
               "doc".equalsIgnoreCase(fileExtension);
    }
}

class ExcelParser implements DocumentParser {
    @Override
    public String parse(File file) {
        return "Excel spreadsheet data extracted";
    }
    @Override
    public boolean supportsFormat(String fileExtension) {
        return "xlsx".equalsIgnoreCase(fileExtension) || 
               "xls".equalsIgnoreCase(fileExtension);
    }
}

// Factory class (non-public)
class DocumentParserFactory {
    private static final List<DocumentParser> parsers = new ArrayList<>();
    
    static {
        parsers.add(new PdfParser());
        parsers.add(new WordParser());
        parsers.add(new ExcelParser());
    }
    
    public static DocumentParser getParser(File file) {
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        
        return parsers.stream()
            .filter(parser -> parser.supportsFormat(fileExtension))
            .findFirst()
            .orElseThrow(() -> new UnsupportedOperationException(
                "No parser available for file type: " + fileExtension));
    }
}
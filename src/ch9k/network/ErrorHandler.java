package ch9k.network;

public interface ErrorHandler {
    /**
     * this method will be called by an EventWriter when 
     * an error happened during writing
     */
    void writingFailed();
    
    /**
     * will be called by an EventReader when it reaches an EOF
     */
    void receivedEOF();
}

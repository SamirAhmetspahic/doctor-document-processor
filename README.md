# doctor-document-processor
an application that processes XML or JSON documents. Application gets input documents from the “input” directory or from a user http POST web service call. 
Application should process “input” folder every 90 seconds.
Elements must be inserted into database and validated.

After successful execution:

    The document should be stored into the “out” directory or if something fails in the “error” directory
    Some info on the process should be stored in the “document_report” table:
        - execution time
        - doctor id
        - error – if any error happens during execution
        - document source (folder or http request)

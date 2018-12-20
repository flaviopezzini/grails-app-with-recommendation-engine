import it.drivek.utils.MarshallingSettings

class BootStrap {

    def quoteImportService
    def carImportService

    def init = { servletContext ->
        quoteImportService.initialize()
        carImportService.initialize()

        MarshallingSettings.setUp();
    }

    def destroy = {
    }
}

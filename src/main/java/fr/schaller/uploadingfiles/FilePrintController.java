package fr.schaller.uploadingfiles;

import fr.schaller.uploadingfiles.print.AjustementPage;
import fr.schaller.uploadingfiles.print.IPrintService;
import fr.schaller.uploadingfiles.print.Imprimante;
import fr.schaller.uploadingfiles.print.Orientation;
import fr.schaller.uploadingfiles.storage.StorageFileNotFoundException;
import fr.schaller.uploadingfiles.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Controller
public class FilePrintController {

	private final StorageService storageService;
	private final IPrintService printService;

	@Autowired
	public FilePrintController(StorageService storageService, IPrintService printService) {
		this.storageService = storageService;
		this.printService = printService;
	}

	@GetMapping("/")
	public String listPrinters(Model model) throws IOException {

		model.addAttribute("cupsservices", printService.listAvailableCupsService().map(printer ->{
				Imprimante imp =new Imprimante(); imp.setUrl(printer.getPrinterURL());imp.setNom(printer.getName()); return imp;
		}).collect(Collectors.toList()));

		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/")
	public String handleFilePrint(@RequestParam("file") MultipartFile file,
								  @RequestParam("printer")URL url,
								  @RequestParam("orientation")boolean estPortrait,
								  @RequestParam("ajuster")boolean ajusterPage,
			RedirectAttributes redirectAttributes) {

		Path filePath =storageService.store(file);
		printService.print(filePath.toFile().getPath(),url, new Orientation(estPortrait), new AjustementPage(ajusterPage));
		storageService.delete(filePath);
		redirectAttributes.addFlashAttribute("message",
				"You successfully printed " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}


}

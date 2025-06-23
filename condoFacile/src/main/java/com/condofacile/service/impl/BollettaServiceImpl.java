package com.condofacile.service.impl;

import com.condofacile.dto.BollettaDTO;
import com.condofacile.dto.BollettaPdfRequestDTO;
import com.condofacile.entity.Bolletta;
import com.condofacile.entity.Utente;
import com.condofacile.error.BollettaNotFoundException;
import com.condofacile.error.BollettaPdfNotCreatedException;
import com.condofacile.repository.BollettaRepository;
import com.condofacile.repository.UtenteRepository;
import com.condofacile.service.BollettaService;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BollettaServiceImpl implements BollettaService {

    private final BollettaRepository bollettaRepository;
    private final UtenteRepository utenteRepository;

    @Override
    public List<BollettaDTO> getAllBollette() {
        List<BollettaDTO> list = new ArrayList<>();
        for (Bolletta bolletta : bollettaRepository.findAll()) {
            BollettaDTO dto = toDTO(bolletta);
            list.add(dto);
        }
        return list;
    }

    @Override
    public BollettaDTO getBollettaById(Integer id) {
        Bolletta bolletta = bollettaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bolletta non trovata con ID: " + id));
        return toDTO(bolletta);
    }

    @Override
    public BollettaDTO createBolletta(BollettaDTO dto) {
        Utente utente = utenteRepository.findById(dto.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + dto.getUtenteId()));

        Bolletta.BollettaBuilder builder = Bolletta.builder();
        builder.descrizione(dto.getDescrizione());
        builder.importo(dto.getImporto());
        builder.dataEmissione(dto.getDataEmissione());
        builder.dataScadenza(dto.getDataScadenza());
        builder.pagata(dto.getPagata() != null && dto.getPagata());
        builder.utente(utente);
        Bolletta bolletta = builder
                .build();

        return toDTO(bollettaRepository.save(bolletta));
    }

    @Override
    public void deleteBolletta(Integer id) {
        if (!bollettaRepository.existsById(id)) {
            throw new BollettaNotFoundException(id);
        }
        bollettaRepository.deleteById(id);
    }

    // Helper method per la conversione
    private BollettaDTO toDTO(Bolletta b) {
        return BollettaDTO.builder()
                .id(b.getId())
                .utenteId(b.getUtente().getId())
                .descrizione(b.getDescrizione())
                .importo(b.getImporto())
                .dataEmissione(b.getDataEmissione())
                .dataScadenza(b.getDataScadenza())
                .pagata(b.getPagata())
                .build();
    }

    public byte[] generateBollettaPdf(BollettaPdfRequestDTO dto) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Recupera utente dalla email
            Utente utente = utenteRepository.findById(Integer.valueOf(dto.getUtenteId()))
                    .orElseThrow(() -> new RuntimeException("Utente non trovato per email: " + dto.getUtenteId()));

            // Logo da classpath
            InputStream imageStream = getClass().getResourceAsStream("/static/img/logoVoltarisEnergy.png");
            if (imageStream != null) {
                byte[] imageBytes = imageStream.readAllBytes();
                ImageData imageData = ImageDataFactory.create(imageBytes);
                Image logo = new Image(imageData);
                logo.setWidth(150);
                document.add(logo);
            } else {
                document.add(new Paragraph("⚠ Logo mancante").setFontColor(com.itextpdf.kernel.colors.ColorConstants.RED));
            }

            // Intestatario bolletta
            document.add(new Paragraph("Intestatario: " + utente.getNome() + " " + utente.getCognome() +
                    " (ID Utente: " + utente.getId() + ")")
                    .setFontSize(12)
                    .setBold()
                    .setMarginBottom(10));

            // Titolo
            document.add(new Paragraph("Bolletta Elettrica")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Tabella con dati bolletta
            Table table = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
                    .setWidth(UnitValue.createPercentValue(100));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            table.addCell(new Cell().add(new Paragraph("Descrizione").setBold()));
            table.addCell(new Cell().add(new Paragraph(dto.getDescrizione())));

            table.addCell(new Cell().add(new Paragraph("Importo").setBold()));
            table.addCell(new Cell().add(new Paragraph("€ " + String.format("%.2f", dto.getImporto()))));

            table.addCell(new Cell().add(new Paragraph("Data Emissione").setBold()));
            table.addCell(new Cell().add(new Paragraph(dto.getDataEmissione().format(formatter))));

            table.addCell(new Cell().add(new Paragraph("Data Scadenza").setBold()));
            table.addCell(new Cell().add(new Paragraph(dto.getDataScadenza().format(formatter))));

            table.addCell(new Cell().add(new Paragraph("Stato").setBold()));
            table.addCell(new Cell().add(new Paragraph(dto.getPagata() != null && dto.getPagata() ? "✅ Pagata" : "❌ Non pagata")));

            document.add(table);

            // Emittente
            document.add(new Paragraph("\nEmittente: Voltaris Energy S.p.A.")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setItalic()
                    .setFontSize(10));

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new BollettaPdfNotCreatedException("Errore durante la generazione del PDF della bolletta: " + e.getMessage());
        }
    }
}
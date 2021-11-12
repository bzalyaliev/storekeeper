package com.example.socks.controller;

import com.example.socks.model.Batch;
import com.example.socks.repository.BatchRepository;
import com.example.socks.repository.BatchEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
public class BatchController {
    private final BatchRepository batchRepository;

    @PostMapping(value = "/income")
    public Integer incomeSocks(@Valid @RequestBody Batch batch) {
        for (int i = 0; i < batch.getQuantity(); i++){
            batchRepository.save(BatchEntity
                    .builder()
                    .color(batch.getColor())
                    .cottonPart(batch.getCottonPart())
                    .quantity(1)
                    .build());
        }
        return 10;
    }

    @PostMapping(value = "/outcome")
    public Integer outcomeSocks(@Valid @RequestBody Batch batch) {
        for (int i = 0; i < batch.getQuantity(); i++) {
            for (BatchEntity batchEntity : batchRepository.findAll()) {
                if (batch.getCottonPart() == batchEntity.getCottonPart()) {
                    batchRepository.delete(batchEntity);
                }
            }
        }
        return 10;
    }

    @GetMapping(value = "/{id}")
    BatchEntity one(@PathVariable Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find sock"));
    }

    @DeleteMapping(value = "/{id}")
    void deleteSock(@PathVariable Long id) {
        batchRepository.deleteById(id);
    }

    @PatchMapping(value = "/{id}")
    public BatchEntity patchBatch(@PathVariable Long id, @RequestBody Batch batch) {
        BatchEntity batchEntity = batchRepository.findById(id).get()
                .setColor(batch.getColor())
                .setCottonPart(batch.getCottonPart())
                .setQuantity((batch.getQuantity()));
        return batchRepository.save(batchEntity);
    }
}

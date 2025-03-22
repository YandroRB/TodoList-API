package com.todolist.todo.service;

import com.todolist.todo.model.Tarea;
import com.todolist.todo.model.Usuario;
import com.todolist.todo.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordatorioService {
    private final TareaRepository tareaRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void enviarRecordatoriosTareas(){
        log.info("Iniciando proceso de envio de recordatorio");
        LocalDateTime ahora= LocalDateTime.now();
        List<Tarea> tareasEncontradas= tareaRepository.findTareasNecesitanRecordatorio(ahora);
        log.info("Se encontraron {} tareas que se enviaran un recordatorio", tareasEncontradas.size());
        for(Tarea tarea: tareasEncontradas){
            Usuario usuario=tarea.getUsuario();
            if(usuario!=null && usuario.getEmail()!=null){
                try {
                    String asunto="Recordatorio: Tarea proxima a vencer";
                    String contenido=generarContenidoRecordatorio(tarea);
                    emailService.enviarEmail(usuario.getEmail(),asunto,contenido);
                    tarea.setRecordatorioActivado(false);
                }catch (Exception e){
                    log.error("Ha ocurrido un error al enviar el recordatorio para la tarea con el id {}: {}",
                            tarea.getIdentificador(), e.getMessage());
                }
            }
        }
        if(!tareasEncontradas.isEmpty()){
            tareaRepository.saveAll(tareasEncontradas);
            log.info("Recordatorios enviados con exito");
        }
    }
    private String generarContenidoRecordatorio(Tarea tarea){
        return String.format("Hola %s \n\n" +
                "Te recordamos que tu tarea %s está a punto de expirar\n\n" +
                "Saludos \n Tu fiel aplicacion de tareas",tarea.getUsuario().getUsername(),tarea.getTitulo());
    }
}

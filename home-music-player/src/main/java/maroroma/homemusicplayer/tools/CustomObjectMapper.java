package maroroma.homemusicplayer.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.files.FileAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomObjectMapper {
    private final ObjectMapper objectMapper;


    public <T> T save(T toSave, FileAdapter target) {
        return Traper.trap(() -> {
            objectMapper.writeValue(target.getOutputStream(), toSave);
            return toSave;
        });
    }

    public <T> T read(Class<T> typeToRead, FileAdapter source) {
        return Traper.trap(() ->
                objectMapper.readValue(source.getInputStream(), typeToRead)
        );
    }

}

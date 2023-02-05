package maroroma.homeserverng.tools.barcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Classe utilitaire pour la gestion de codes barres
 */
@Slf4j
public class BarCodeReader {

    /**
     * Méthode utilitaire pour la lecture d'un code barre à partir d'un stream
     * @param inputStream
     * @return un optional potentiellement vide (si rien de trouver ou si erreur)
     */
    public static Optional<String> readBarCode(InputStream inputStream) {
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(ImageIO.read(inputStream))));

            Result result  = new MultiFormatReader().decode(binaryBitmap);

            return Optional.ofNullable(result)
                    .map(Result::getText);

        } catch (IOException | NotFoundException e) {
            log.error("Impossible de récupérer le code barre", e);

            return Optional.empty();
        }

    }

}

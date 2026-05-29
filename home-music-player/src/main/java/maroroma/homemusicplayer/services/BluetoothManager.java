package maroroma.homemusicplayer.services;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothAdapter;
import lombok.extern.slf4j.Slf4j;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.types.Variant;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class BluetoothManager {

//    └─/org
//  └─/org/bluez
//    └─/org/bluez/hci0
//      └─/org/bluez/hci0/dev_3C_38_24_52_3B_99


    public boolean bluetoothStatus() {

        try (DBusConnection connection = DBusConnectionBuilder.forSystemBus().build()) {

            String bluezService = "org.bluez";
            String adapterPath = "/org/bluez/hci0"; // Premier adaptateur Bluetooth du Raspberry Pi
            String adapterInterface = "org.bluez.Adapter1";

            // 2. On récupère l'objet BlueZ via l'interface des Propriétés DBus
            Properties properties = connection.getRemoteObject(
                    bluezService,
                    adapterPath,
                    Properties.class
            );

            // 3. On extrait la propriété "Powered"
            // BlueZ renvoie un Variant contenant un Boolean
            Variant<?> poweredVariant = properties.Get(adapterInterface, "Powered");
            Boolean isPowered = (Boolean) poweredVariant.getValue();

            // 4. Affichage du résultat
            if (isPowered != null && isPowered) {
                log.info("🟢 Le Bluetooth est ACTIVÉ sur le Raspberry Pi.");
                return true;
            } else {
                log.info("🔴 Le Bluetooth est DÉSACTIVÉ sur le Raspberry Pi.");
                return false;
            }

        } catch (Exception e) {
            log.error("❌ Erreur lors de la communication avec BlueZ via DBus : ", e);
            log.error("Vérifiez que le service bluetooth est actif (`sudo systemctl status bluetooth`)");
            return false;
        }


    }

    public boolean activateBluetooth() {
        try (DBusConnection connection = DBusConnectionBuilder.forSystemBus().build()) {

            String bluezService = "org.bluez";
            String adapterPath = "/org/bluez/hci0"; // Premier adaptateur Bluetooth du Raspberry Pi
            String adapterInterface = "org.bluez.Adapter1";


            // 1. Récupérer l'interface des Propriétés pour l'adaptateur
            Properties properties = connection.getRemoteObject(
                    bluezService,
                    adapterPath,
                    Properties.class
            );

            log.info("🔄 Activation du Bluetooth en cours...");

            // 2. Modifier la propriété "Powered" à true
            // DBus nécessite d'encapsuler la valeur dans un Variant en spécifiant son type (généralement déduit en Java)
            Variant<Boolean> turnOn = new Variant<>(true);
            properties.Set(adapterInterface, "Powered", turnOn);

            log.info("🟢 Le Bluetooth a été activé avec succès !");
            return true;

        } catch (Exception e) {
            log.error("❌ Impossible d'activer le Bluetooth.", e);
            log.error("Vérifiez vos permissions. L'application a-t-elle les droits requis (sudo / groupe bluetooth) ?");
            return false;
        }
    }

    public boolean getstatus(boolean session) {
        try {
            // 1. Initialiser le gestionnaire BlueZ
            DeviceManager deviceManager = DeviceManager.createInstance(session);

            // 2. Récupérer le premier adaptateur disponible (ex: hci0)
            BluetoothAdapter adapter = deviceManager.getAdapter();

            if (adapter == null) {
                log.error("🔴 Aucun adaptateur Bluetooth n'a été détecté sur ce Raspberry Pi.");
                return false;
            }

            log.info("ℹ️ Adaptateur trouvé : " + adapter.getDeviceName() + " [" + adapter.getAddress() + "]");

            // 3. Vérifier si le Bluetooth est activé (Powered)
            if (adapter.isPowered()) {
                log.info("🟢 Le Bluetooth est actuellement ACTIVÉ.");

                // Petit bonus : On peut aussi vérifier s'il est en mode découverte/appairage
                log.info("👉 Mode visible (Discoverable) : " + (adapter.isDiscoverable() ? "Oui" : "Non"));
                log.info("👉 Mode appairage (Pairable)   : " + (adapter.isPairable() ? "Oui" : "Non"));

                return true;
            } else {
                log.error("🔴 Le Bluetooth est actuellement DÉSACTIVÉ.");
                return false;
            }

        } catch (Exception e) {
            log.error("❌ Erreur lors de la lecture du statut Bluetooth : ", e);
            return false;
        }
    }

}

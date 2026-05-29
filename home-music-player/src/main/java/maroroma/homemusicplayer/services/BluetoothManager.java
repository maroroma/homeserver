package maroroma.homemusicplayer.services;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothAdapter;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.bluetooth.BluetoothStatus;
import org.bluez.Agent1;
import org.bluez.AgentManager1;
import org.bluez.exceptions.BluezCanceledException;
import org.bluez.exceptions.BluezRejectedException;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.ObjectPath;
import org.freedesktop.dbus.types.UInt16;
import org.freedesktop.dbus.types.UInt32;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class BluetoothManager {

//    └─/org
//  └─/org/bluez
//    └─/org/bluez/hci0
//      └─/org/bluez/hci0/dev_3C_38_24_52_3B_99

    public BluetoothStatus getStatus() {
        try {
            // 1. Initialiser le gestionnaire BlueZ
            DeviceManager deviceManager = DeviceManager.createInstance(false);

            // 2. Récupérer le premier adaptateur disponible (ex: hci0)
            BluetoothAdapter adapter = deviceManager.getAdapter();

            if (adapter == null) {
                return BluetoothStatus.off("Aucun adaptateur Bluetooth n'a été détecté sur ce Raspberry Pi.");
            }

            log.info("ℹ️ Adaptateur trouvé : " + adapter.getDeviceName() + " [" + adapter.getAddress() + "]");

            // 3. Vérifier si le Bluetooth est activé (Powered)
            if (adapter.isPowered()) {
                return BluetoothStatus.builder()
                        .on(true)
                        .discoverable(adapter.isDiscoverable())
                        .pairable(adapter.isPairable())
                        .build();
            } else {
                return BluetoothStatus.off("Le Bluetooth est actuellement DÉSACTIVÉ.");
            }

        } catch (Exception e) {
            log.error("❌ Erreur lors de la lecture du statut Bluetooth : ", e);
            return BluetoothStatus.off("Erreur lors de la lecture du statut Bluetooth");
        }
    }

    public BluetoothStatus powerOn() {
        try {
            // 1. Initialiser le gestionnaire BlueZ
            DeviceManager deviceManager = DeviceManager.createInstance(false);

            // 2. Récupérer le premier adaptateur disponible (ex: hci0)
            BluetoothAdapter adapter = deviceManager.getAdapter();

            if (adapter == null) {
                return BluetoothStatus.off("Aucun adaptateur Bluetooth n'a été détecté sur ce Raspberry Pi.");
            }

            log.info("ℹ️ Adaptateur trouvé : " + adapter.getDeviceName() + " [" + adapter.getAddress() + "]");

            // 3. Activer l'adaptateur, la visibilité et l'appairage
            adapter.setPowered(true);
            adapter.setDiscoverable(true);
            adapter.setPairable(true);

            // Optionnel : Temps de visibilité illimité (0) ou défini (ex: 120 secondes)
            // TODO : rendre paramétrable
            adapter.setDiscoverableTimeout(0);

            // 4. Utiliser l'agent "NoInputNoOutput" fourni par la bibliothèque !
            // Cet agent accepte tout par défaut sans interaction utilisateur.
            AutoAcceptAgent autoAcceptAgent = new AutoAcceptAgent();

            deviceManager.getDbusConnection().exportObject(autoAcceptAgent);

            AgentManager1 agentManager = deviceManager.getDbusConnection().getRemoteObject(
                    "org.bluez",
                    "/org/bluez",
                    AgentManager1.class
            );

//            ObjectPath agentPath = new ObjectPath(customAgent.getObjectPath());
            var dbusPathForAgent = DBusPath.of(autoAcceptAgent.getObjectPath());
            agentManager.RegisterAgent(dbusPathForAgent, "NoInputNoOutput");
            agentManager.RequestDefaultAgent(dbusPathForAgent);

            return BluetoothStatus.builder()
                    .on(adapter.isPowered())
                    .discoverable(adapter.isDiscoverable())
                    .pairable(adapter.isPairable())
                    .build();

        } catch (Exception e) {
            log.error("❌ Erreur lors du lancement de l'appairage ", e);
            return BluetoothStatus.off("Erreur lors du lancement de l'appairage");
        }
    }

    public BluetoothStatus powerOff() {
        try {
            // 1. Initialiser le gestionnaire BlueZ
            DeviceManager deviceManager = DeviceManager.createInstance(false);

            // 2. Récupérer le premier adaptateur disponible (ex: hci0)
            BluetoothAdapter adapter = deviceManager.getAdapter();

            if (adapter == null) {
                return BluetoothStatus.off("Aucun adaptateur Bluetooth n'a été détecté sur ce Raspberry Pi.");
            }

            log.info("ℹ️ Adaptateur trouvé : " + adapter.getDeviceName() + " [" + adapter.getAddress() + "]");

            // 3. Activer l'adaptateur, la visibilité et l'appairage
            adapter.setPowered(false);
            adapter.setDiscoverable(false);
            adapter.setPairable(false);

            return BluetoothStatus.builder()
                    .on(adapter.isPowered())
                    .discoverable(adapter.isDiscoverable())
                    .pairable(adapter.isPairable())
                    .build();

        } catch (Exception e) {
            log.error("❌ Erreur lors de l'arrêt du bluetooth ", e);
            return BluetoothStatus.off("Erreur lors de l'arrêt du bluetooth");
        }
    }

    // 1. On implémente directement l'interface "Agent1" fournie par bluez-dbus
    public static class AutoAcceptAgent implements Agent1 {



        @Override
        public void Release() {
            log.info("AutoAcceptAgent::Release");
        }

        @Override
        public String RequestPinCode(DBusPath _device) throws BluezRejectedException, BluezCanceledException {
            log.info("AutoAcceptAgent::RequestPinCode");

            return "";
        }

        @Override
        public void DisplayPinCode(DBusPath _device, String _pincode) throws BluezRejectedException, BluezCanceledException {
            log.info("AutoAcceptAgent::DisplayPinCode");

        }

        @Override
        public UInt32 RequestPasskey(DBusPath _device) throws BluezRejectedException, BluezCanceledException {
            log.info("AutoAcceptAgent::RequestPasskey");

            return null;
        }

        @Override
        public void DisplayPasskey(DBusPath _device, UInt32 _passkey, UInt16 _entered) {
            log.info("AutoAcceptAgent::DisplayPasskey");

        }

        @Override
        public void RequestConfirmation(DBusPath _device, UInt32 _passkey) throws BluezRejectedException, BluezCanceledException {
            log.info("AutoAcceptAgent::RequestConfirmation");

        }

        @Override
        public void RequestAuthorization(DBusPath _device) throws BluezRejectedException, BluezCanceledException {
            log.info("AutoAcceptAgent::RequestAuthorization");

        }

        @Override
        public void AuthorizeService(DBusPath _device, String _uuid) throws BluezRejectedException, BluezCanceledException {
            log.info("AutoAcceptAgent::AuthorizeService");

        }

        @Override
        public void Cancel() {
            log.info("AutoAcceptAgent::Cancel");
        }

        @Override
        public String getObjectPath() {
            return "/app/bluetooth/agent";
        }
    }

}

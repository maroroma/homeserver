package maroroma.homemusicplayer.services;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.bluetooth.wrapper.AgentManager;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothAdapter;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.bluetooth.BluetoothStatus;
import maroroma.homemusicplayer.tools.Traper;
import org.bluez.Agent1;
import org.bluez.MediaControl1;
import org.bluez.MediaPlayer1;
import org.bluez.exceptions.BluezCanceledException;
import org.bluez.exceptions.BluezRejectedException;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.types.UInt16;
import org.freedesktop.dbus.types.UInt32;
import org.springframework.stereotype.Service;


// TODO : factoriser le code de récupération de l'adapter
// voir si on peut pas jouer avec AgentHandler, qui introduit la notion d'events
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

            // TODO : lister device connectés via deviceManager.getDevices(), alterer le  BluetoothStatus en ce sens

            // 2. Récupérer le premier adaptateur disponible (ex: hci0)
            BluetoothAdapter adapter = deviceManager.getAdapter();

            if (adapter == null) {
                return BluetoothStatus.off("Aucun adaptateur Bluetooth n'a été détecté sur ce Raspberry Pi.");
            }

            log.info("ℹ️ Adaptateur trouvé : " + adapter.getDeviceName() + " [" + adapter.getAddress() + "]");

            // 3. Vérifier si le Bluetooth est activé (Powered)
            if (adapter.isPowered()) {
                return BluetoothStatus.builder()
                        .on(adapter.isPowered())
                        .discoverable(adapter.isDiscoverable())
                        .pairable(adapter.isPairable())
                        .connectedDevices(deviceManager.getDevices().stream().map(aDevice -> maroroma.homemusicplayer.model.bluetooth.BluetoothDevice.builder()
                                .connected(aDevice.isConnected())
                                .name(aDevice.getAlias())
                                .build()).toList())
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
            adapter.setDiscoverableTimeout(120);

            // 4. Utiliser l'agent "NoInputNoOutput" fourni par la bibliothèque !
            // Cet agent accepte tout par défaut sans interaction utilisateur.
            AutoAcceptAgent autoAcceptAgent = new AutoAcceptAgent();

            if (Traper.trapToBoolean(() -> deviceManager.getDbusConnection().exportObject(autoAcceptAgent))) {
                log.info("autoAcceptAgent exported");
            } else {
                log.warn("autoAcceptAgent already exported");
            }

            AgentManager agentManager = new AgentManager(deviceManager.getDbusConnection());

//            ObjectPath agentPath = new ObjectPath(customAgent.getObjectPath());
            if (Traper.trapToBoolean(() -> agentManager.registerAgent(autoAcceptAgent.getObjectPath(), "NoInputNoOutput"))) {
                log.info("agent registered");
            } else {
                log.warn("agent not registered or already registered");
            }
            if (Traper.trapToBoolean(() -> agentManager.requestDefaultAgent(autoAcceptAgent.getObjectPath()))) {
                log.info("agent requested as default");
            } else {
                log.warn("agent not requested as default or already registered");
            }

            return BluetoothStatus.builder()
                    .on(adapter.isPowered())
                    .discoverable(adapter.isDiscoverable())
                    .pairable(adapter.isPairable())
                    .connectedDevices(deviceManager.getDevices().stream().map(aDevice -> maroroma.homemusicplayer.model.bluetooth.BluetoothDevice.builder()
                            .connected(aDevice.isConnected())
                            .name(aDevice.getAlias())
                            .build()).toList())
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

            if (!adapter.isPowered()) {
                return BluetoothStatus.off("Bluetooth déjà off");
            }

            // 3. Activer l'adaptateur, la visibilité et l'appairage
            adapter.setPowered(false);
            // à priori pas besoin une fois que le bluetooth est off
//            adapter.setDiscoverable(false);
//            adapter.setPairable(false);
//            deviceManager.getDevices().forEach(BluetoothDevice::disconnect);

            return BluetoothStatus.builder()
                    .on(adapter.isPowered())
                    .discoverable(adapter.isDiscoverable())
                    .pairable(adapter.isPairable())
                    .connectedDevices(deviceManager.getDevices().stream().map(aDevice -> maroroma.homemusicplayer.model.bluetooth.BluetoothDevice.builder()
                            .connected(aDevice.isConnected())
                            .name(aDevice.getAlias())
                            .build()).toList())
                    .build();

        } catch (Exception e) {
            log.error("❌ Erreur lors de l'arrêt du bluetooth ", e);
            return BluetoothStatus.off("Erreur lors de l'arrêt du bluetooth");
        }
    }

    public boolean next() {
        try {
            // 1. Initialiser le gestionnaire BlueZ
            DeviceManager deviceManager = DeviceManager.createInstance(false);

            // 2. Récupérer le premier adaptateur disponible (ex: hci0)
            BluetoothAdapter adapter = deviceManager.getAdapter();

            if (adapter == null) {
                return false;
            }

            log.info("ℹ️ Adaptateur trouvé : " + adapter.getDeviceName() + " [" + adapter.getAddress() + "]");

            var firstConnectedDevice = deviceManager.getDevices()
                    .stream()
                    .filter(BluetoothDevice::isConnected)
                    .findFirst();

            if (firstConnectedDevice.isEmpty()) {
                return false;
            }

            var connectedDevice = firstConnectedDevice.get();

            String deviceDbusPath = connectedDevice.getRawDevice().getObjectPath();

            // 2. Récupérer l'interface de contrôle des médias (MediaControl1) liée à cet appareil
            MediaPlayer1 mediaControl = deviceManager.getDbusConnection().getRemoteObject(
                    "org.bluez",
                    deviceDbusPath,
                    MediaPlayer1.class
            );

            mediaControl.Next();
            return true;

        } catch (Exception e) {
            log.error("❌ Erreur lors changement de track ", e);
            return false;
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

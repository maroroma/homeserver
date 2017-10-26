export class ApiConstants {
    public static readonly ROOT_API = 'api/';


    public static readonly ADMIN_API = ApiConstants.ROOT_API + 'administration/';
    public static readonly ADMIN_MODULES_API = ApiConstants.ADMIN_API + 'modules';
    public static readonly ADMIN_MODULE_API = ApiConstants.ADMIN_API + 'module';
    public static readonly ADMIN_CONFIGS_API = ApiConstants.ADMIN_API + 'configs';
    public static readonly ADMIN_CONFIG_API = ApiConstants.ADMIN_API + 'config';
    public static readonly ADMIN_REPOS_API = ApiConstants.ADMIN_API + 'repos';
    public static readonly ADMIN_REPO_API = ApiConstants.ADMIN_API + 'repo';
    public static readonly ADMIN_CACHES_API = ApiConstants.ADMIN_API + 'caches';
    public static readonly ADMIN_SHUT_API = ApiConstants.ADMIN_API + 'server/stop';

    public static readonly SEEDBOX_API = ApiConstants.ROOT_API + 'seedbox/';
    public static readonly SEEDBOX_TODO_API = ApiConstants.SEEDBOX_API + 'todo/';
    public static readonly SEEDBOX_TORRENTS_API = ApiConstants.SEEDBOX_API + 'torrents';
    public static readonly SEEDBOX_TODO_COMPLETED_FILES_API = ApiConstants.SEEDBOX_API + 'todo/completedtorrents';
    public static readonly SEEDBOX_TARGETS_API = ApiConstants.SEEDBOX_API + 'todo/targets';
    public static readonly SEEDBOX_SORT_API = ApiConstants.SEEDBOX_API + 'todo/sortedfile';


    public static readonly NETWORK_API = ApiConstants.ROOT_API + 'network/';
    public static readonly NETWORK_SERVERS_API = ApiConstants.NETWORK_API + 'servers';
    public static readonly NETWORK_SERVER_API = ApiConstants.NETWORK_API + 'server';
    public static readonly NETWORK_AVAILABLE_SERVER_API = ApiConstants.NETWORK_API + 'availableservers';

    public static readonly FILEMANAGER_API = ApiConstants.ROOT_API + 'filemanager/';
    public static readonly FILEMANAGER_DIRECTORY_API = ApiConstants.FILEMANAGER_API + 'directory';
    public static readonly FILEMANAGER_DIRECTORIES_API = ApiConstants.FILEMANAGER_API + 'directories';
    public static readonly FILEMANAGER_FILES_API = ApiConstants.FILEMANAGER_API + 'files';
    public static readonly FILEMANAGER_ROOT_DIRECTORIES_API = ApiConstants.FILEMANAGER_API + 'rootdirectories';

    public static readonly MUSIC_API = ApiConstants.ROOT_API + 'music/';
    public static readonly MUSIC_WORKING_DIR_API = ApiConstants.MUSIC_API + 'workingdirectories';


    public static readonly REDUCER_API = ApiConstants.ROOT_API + 'reducer/';
    public static readonly REDUCER_REDUCED_IMAGES_API = ApiConstants.REDUCER_API + 'reducedImages';
    public static readonly REDUCER_REDUCED_IMAGE_API = ApiConstants.REDUCER_API + 'reducedImage';
    public static readonly REDUCER_REDUCED_FULL_SIZE_IMAGE_API = ApiConstants.REDUCER_API + 'fullSizeImage';
    public static readonly REDUCER_REDUCED_FIND_CONTACTS_API = ApiConstants.REDUCER_API + '/mail/contacts';
    public static readonly REDUCER_REDUCED_SEND_MAIL_API = ApiConstants.REDUCER_API + '/mail';
}

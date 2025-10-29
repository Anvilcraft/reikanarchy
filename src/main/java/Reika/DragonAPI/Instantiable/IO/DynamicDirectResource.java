package Reika.DragonAPI.Instantiable.IO;

import java.io.IOException;
import java.io.InputStream;

public class DynamicDirectResource extends DirectResource {
    private final RemoteSourcedAsset asset;

    public DynamicDirectResource(RemoteSourcedAsset a) {
        super(a.path);

        asset = a;
    }

    @Override
    protected InputStream calcStream() {
        try {
            return asset.getData();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

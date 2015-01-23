package com.dhcc.gis.dgs{
    
    import com.esri.ags.*;
    import com.esri.ags.geometry.*;
    import com.esri.ags.layers.*;
    import com.esri.ags.layers.supportClasses.*;
    
    import flash.net.URLRequest;
    
    import mx.controls.*;
    
    public class DTileLayer extends TiledMapServiceLayer{

        private var _tileInfo:TileInfo = new TileInfo();
        private var url:String;
        private var prefix:String;
        
        public function DTileLayer(prefix:String){
            super();
            this.prefix = prefix;
            buildTileInfo(null);
            setLoaded(true);
        }
        
        override public function get fullExtent():Extent{
            return new Extent(271987.18334,122920.58593,724459.12036,653400.07714,getSR());
        }
        
        override public function get initialExtent():Extent{
            return new Extent(371987.18334,252920.58593,624459.12036,423400.07714,getSR());
        }
        
        override public function get spatialReference():SpatialReference{
            return getSR();
        }
        
        override public function get tileInfo():TileInfo{
            return this._tileInfo;
        }
        
        override public function get units():String{
            return Units.METERS;
        }
        
        override protected function getTileURL(level:Number,row:Number,col:Number):URLRequest{
            return new URLRequest(prefix+"?REQUEST=GetTile&TILEMATRIX="+level+"&TILEROW="+row+"&TILECOL="+col);
        }
        
        private function getSR():SpatialReference{
            return new SpatialReference(NaN,"PROJCS[\"beijingXY\",GEOGCS[\"GCS_Beijing_1954\",DATUM[\"D_Beijing_1954\",SPHEROID[\"Krasovsky_1940\",6378245.0,298.3]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",116.32429],PARAMETER[\"Scale_Factor\",1.00125],PARAMETER[\"Latitude_Of_Origin\",37.16071],UNIT[\"Meter\",1.0]]");
        }
        
        private function buildTileInfo(config:XML):void{
            _tileInfo.height = 256;
            _tileInfo.width = 256;
			//向下加 向左加
            _tileInfo.origin = new MapPoint(228541-300-160+1164,460141-780-200-279);
			var r:Number=896.5851325035984;//分辨率
			var s:Number=3400000.0;//比例尺
			
            _tileInfo.spatialReference = getSR();
            _tileInfo.lods = [
                new LOD(12,r,s),
                new LOD(11,r/Math.pow(2,1), s/Math.pow(2,1)),
                new LOD(10,r/Math.pow(2,2), s/Math.pow(2,2)),
                new LOD(9, r/Math.pow(2,3), s/Math.pow(2,3)),
                new LOD(8, r/Math.pow(2,4), s/Math.pow(2,4)),
                new LOD(7, r/Math.pow(2,5), s/Math.pow(2,5)),
                new LOD(6, r/Math.pow(2,6), s/Math.pow(2,6)),
                new LOD(5, r/Math.pow(2,7), s/Math.pow(2,7)),
                new LOD(4, r/Math.pow(2,8), s/Math.pow(2,8)),
				new LOD(3, r/Math.pow(2,9), s/Math.pow(2,9)),
                new LOD(2, r/Math.pow(2,10),s/Math.pow(2,10)),
                new LOD(1, r/Math.pow(2,11),s/Math.pow(2,11)),
                new LOD(0, r/Math.pow(2,12),s/Math.pow(2,12))
            ];
        }
        
    }
    
}
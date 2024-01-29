package com.amiiboroom.ordercollector.util.selenium;

import com.amiiboroom.ordercollector.util.enums.SiteType;

public class OrderCollectorFactory {

    public static OrderCollector newInstance(SiteType siteType, SeleniumUtil seleniumUtil) {
        OrderCollector orderCollector;

        switch(siteType) {
            case NAVER -> orderCollector = new NaverOrderCollector(seleniumUtil);
            case SOFRANOMALL -> orderCollector = new SofranoMallOrderCollector(seleniumUtil);
            case GAMEWOORI -> orderCollector = new GameWooriOrderCollector(seleniumUtil);
            case DAEWONSHOP -> orderCollector = new DaewonShopOrderCollector(seleniumUtil);
            case BANDAIMALL -> orderCollector = new BandaiMallOrderCollector(seleniumUtil);
            case AUCTION -> orderCollector = new AuctionOrderCollector(seleniumUtil);
            case GMARKET -> orderCollector = new GMarketOrderCollector(seleniumUtil);
            case COUPANG -> orderCollector = new CoupangOrderCollector(seleniumUtil);
            case TMON -> orderCollector = new TmonOrderCollector(seleniumUtil);
            case YES24 -> orderCollector = new Yes24OrderCollector(seleniumUtil);
            case COMICSART -> orderCollector = new ComicsArtOrderCollector(seleniumUtil);
            case GUNDAMBOOM -> orderCollector = new GundamBoomOrderCollector(seleniumUtil);
            case KYOUMASHOP -> orderCollector = new KyoumaShopOrderCollector(seleniumUtil);
            case H2MALL -> orderCollector = new H2MallOrderCollector(seleniumUtil);
            case AMAZON -> orderCollector = new AmazonOrderCollector(seleniumUtil);
            case PLAYASIA -> orderCollector = new PlayAsiaOrderCollector(seleniumUtil);
            case AMIAMI -> orderCollector = new AmiAmiOrderCollector(seleniumUtil);
            case LIMITEDRUN -> orderCollector = new LimitedRunOrderCollector(seleniumUtil);
            case MERCARI -> orderCollector = new MerCariOrderCollector(seleniumUtil);
            case ALIEXPRESS -> orderCollector = new AliExpressOrderCollector(seleniumUtil);
            default -> orderCollector = null;
        }

        return orderCollector;
    }

}

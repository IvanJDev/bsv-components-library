package com.nchain.jcl.net.unit.protocol.serialization.largeMsgs

import com.nchain.jcl.net.protocol.config.ProtocolConfig
import com.nchain.jcl.net.protocol.config.ProtocolConfigBuilder
import com.nchain.jcl.net.protocol.messages.HeaderMsg
import com.nchain.jcl.net.protocol.messages.PartialBlockTxnMsg
import com.nchain.jcl.net.protocol.messages.common.Message
import com.nchain.jcl.net.protocol.serialization.common.DeserializerContext
import com.nchain.jcl.net.protocol.serialization.largeMsgs.BigBlockTxnDeserializer
import com.nchain.jcl.net.unit.protocol.tools.ByteArrayArtificalStreamProducer
import com.nchain.jcl.tools.bytes.ByteArrayReader
import com.nchain.jcl.tools.bytes.ByteArrayWriter
import io.bitcoinsv.bitcoinjsv.core.Utils
import io.bitcoinsv.bitcoinjsv.params.MainNetParams
import io.bitcoinsv.bitcoinjsv.params.Net
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A Test to Deserialize a BlockTxN Msg using the BigBlockTxsDeserializer, which is a Large Deserializer
 */
class BigBlockTxnDeserializerTest extends Specification {

    private static final String BLOCKTXN_BYTES = "12bc786e113e85cdd1c4a87334dc2b94c983c734c0f0f40700000000000000000f02000000012768da2110f7f141eecebdf52fb35410fe0ebfe14dd8f5a603d0c8c42ec5573d010000006b483045022100cfa18fa453682c12b63079a13c50c92c3f420142552209885cf31ecfe51ce42802202b2a04aea10c910b91c5ab8acb3220deee392de5e428372ab2e35a26aab438aa41210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1066326339313264303437336632326365f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac000000000100000001ddee8fede4cc937f459643622cf22c311ad31b68377e15e580134a3f8b543f41020000006b483045022100f6c8e2152b6530d819e2f9b704de151c00f5405845e637346c6de7bd8619e35a02207865200354acbd8d53358f108b84b2c6a99c40c3d391b8aa71a4748dbde3491d412103107feff22788a1fc8357240bf450fd7bca4bd45d5f8bac63818c5a7b67b03876ffffffff020000000000000000fdff00006a0372756e01050c63727970746f6669676874734ce87b22696e223a312c22726566223a5b22323237373530643430303665323939306464346236633239356439306332353538663130663235623032343436373737623130666361303866353565343862335f6f31225d2c226f7574223a5b2261383137363265623933303565633338373939633434626564333763303564323963626637646632653566643936646661343764386562373133626464396633225d2c2264656c223a5b5d2c22637265223a5b5d2c2265786563223a5b7b226f70223a2243414c4c222c2264617461223a5b7b22246a6967223a307d2c22616374222c5b305d5d7d5d7d11010000000000001976a9143b80a2d74a2b6dcd2f15fdea0d14aa58736de6d788ac000000000200000001015addfd834e3b45c6b0ea044e80dc1871572491f277fbdff95f027b5e4b572d010000006b483045022100d83d5f1745d3f642a1bb14b6239a9d5480baadc55cf3686257130287b0f0d5d5022028ff7e041a22c9afc6e4f2c1bffadb7881f68719923591772b9d045a22cd87b441210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1037626162663832383965376339353465f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac0000000002000000015147bcddf7007b1050a3d196d61b4f65ad6b57a167acbd8bd51a44ed3b30eb06010000006b4830450221009c7ce9344b17d86ec169c85debb696eac3d311b8183439f803b72cfe3374fa9602202d09944e96de9f2f2da2e51ef2a6fbeff690b3f5648f2a8e8454a9b21959c70741210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1033613030633137313765663638376166f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac000000000200000001489ca53d5f3a20ed3ce0a039069ee104cd58fec12b9edeb204e2ea24509a5a90010000006a47304402204cd584f6e2104f5e0c76c6724f54a9b71947b3ca8c83f85bf09dac0b96a3b305022001f327e413c5130eb9ed43046f13d9658863b63de6c3040407afb7cb32e9927541210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1034613935386432653630373232333037f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac0000000002000000018892f099aa347fb3a120403d9f1d1e7708354c8b2c9ea00af9b01e86e870a03e010000006a47304402202ab73c192b716a4e19069d830f4ad2617fbf2483042c96a1a3d7aa12ee64d2c602202aab61734e3cdf259c43bcd138b9a2dcf308198c1b6669efb7a470481377646a41210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1033666235393430346331356630643034f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac000000000200000001ce8683b80da65b25bcef024ab6d1449d32d91d36369625896d7a0ef6de058d33010000006a47304402202aebda409aefa2f2d5c48e185c487df36289f868716bce40730aa9cd1aca4fb2022064c07f108dcb86a6a337deba105c4a72411eed1032b591dd90c4a7e25b94267541210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1030616262616164313864373665373563f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac000000000100000003ddee8fede4cc937f459643622cf22c311ad31b68377e15e580134a3f8b543f41010000006a4730440220115e46f33725f8133b2a99b422e0f5780dfdb2b1a44c12dc12ba96402e452a5902202c2f69ab074dc2679dbf9dcce918d7c3aa0cf917b75e56240c713c27ce2e9983412103107feff22788a1fc8357240bf450fd7bca4bd45d5f8bac63818c5a7b67b03876ffffffffbf9fee44d7c4bd0233fcd83f41fda3a54225cddb630bcd0cd79a817b16c7f8f1010000006a47304402205cc07ae0a32ae21883dbcc9b34e31b4cd82b083d7788face1740647a304c9462022016c105efa43d0fac5a982b0f578f25b31e78f2ff428379ff39509e88e49d90c0412103107feff22788a1fc8357240bf450fd7bca4bd45d5f8bac63818c5a7b67b03876ffffffffddee8fede4cc937f459643622cf22c311ad31b68377e15e580134a3f8b543f41030000006b483045022100c41ef432b24b879a496de3f8ce0458af5afa68c8b5d3cb31d58764b1296c61dd02204557914ee1faa1c20d35edacd17782b29319ec780b3978e8c26b5384f8758b76412103c134c904118b148d32492cd17d1183088f708a3e4a7429f3260ff51b9e72c6ccffffffff030000000000000000fd7705006a0372756e01050c63727970746f6669676874734d5f057b22696e223a322c22726566223a5b22633635346330383863386561353730663738626164326437616561616665303230323163623832633630626237313631343363326233633762316365663337385f6f31222c22333832373133633262376162663537306231643334346562636636623738393664386335643866643633363237333563356665373265643739333832393637305f6f31222c22366531396336386639376239626364666335326264336438663763663666393134373062353031336465316432646464366439323031633464313066636366305f6f31222c22336136376365633363313662646238343762393732626565326663316330373137633539656463616537626635663438633931666563636661363335616633335f6f31222c22313465323738633638666635323165303931366164376337313361653461303135366537363336316462643362326233353764666236303238653064636137615f6f31222c22613738663561366437326637383731316536366336323131666262643061306266643135616439316264643030343034393238613966616363363364613664395f6f31222c22323237373530643430303665323939306464346236633239356439306332353538663130663235623032343436373737623130666361303866353565343862335f6f31222c22633263346339373165383562343939633239613861623231343866643332346665313262353530623866346635373635386134363836653031316438666435385f6f31222c22616138323061316262656535636163393932333963623566346531323739616536656232646532346236303163663864323232363261363638363763333131615f6f31222c22316235613339353532663139616436366262306366306264383564366238663935656633613266656561346235656236343033333330363165303664316461655f6f36222c22353736323365353566386363383331633164336334326564646436343535653863653339383136616265343433653732353638373733643336306531333233615f6f31222c22373166626133383633343162393332333830656335626665646333613430626365343364343937346465636463393463343139613934613863653564666332335f6f31222c22363161653132323165646438626431646438336332326461326232616237643131346139313239363439366365336664306562613737333236623638613238335f6f31222c22616662363630646638613434363864343162386332623736663364636533346630663837396462366564306132643230303964363037303232333765636264345f6f31222c22643739343235613432323362323838336639303333653137323538373836363766636438343863633565633532376366626365353162363633333735323834315f6f31225d2c226f7574223a5b2265363932636433343535613264386532623266643035376162623366316431386364363461646262636466363834333835373865643865616232326439366430225d2c2264656c223a5b2265356233313032666138353162643465313561363963376438386461303330396563363031303435646264373432346138623761393564323865376337383434225d2c22637265223a5b5d2c2265786563223a5b7b226f70223a2243414c4c222c2264617461223a5b7b22246a6967223a307d2c227265736f6c7665222c5b2233653761383737383161353532303263333030666233316637653133343561363863346462633635313338336435363734386238653565633965386434353432222c313632323830323835323337342c313632323830323838323337345d5d7d5d7d22020000000000001976a9143b80a2d74a2b6dcd2f15fdea0d14aa58736de6d788ac168d0000000000001976a9144f7d6a485e09770f947c0ba38d15050a5a80b6fa88ac000000000200000001369372d1531bb456e531f4976bd5e5976d8f399864ad7c7e58f02a1414660b46010000006a473044022026d3b5ff01b5b4634a34fcef915def8d570b65ae28c42a9922c6e6020608f66f022007c0470861660b6114a07216f42ce91950baa4639be21b8dee6a2cf4ffa4fb4341210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1066373232616339633035653761363735f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac000000000200000001fc46ecbc4d7ce46e51e03fff5524c8f34bd7eda8576781c2daefa9ed02775d2d010000006a47304402205e07e166a599ed58289a473f7c4ae0a18d183711fa42ec03d27dc2e69bc225d002207e4f8de193d12af7801b1cbc495df1c9ed425d6d073047e51f72ccaf6e8c06d341210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1039663564313530363761666163643361f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac000000000100000001989d18c62cf4046b039b32e319cb82fc96e64cea77085e9d69f931363c3b50d2020000006b483045022100e72095942809a9e259ca10ee9284dd2439b454778fd44d2f16cb23c00f3acfb902202734e1a51d26caad7b5bf575af452dbaca5751add5f79e5e3bb21fa0f2ada6aa4121033b1d1fc5d40f597fd5051ef6e3b2f49e16812c415b7aded7360052a5b7cde018ffffffff020000000000000000fd0001006a0372756e01050c63727970746f6669676874734ce97b22696e223a312c22726566223a5b22323237373530643430303665323939306464346236633239356439306332353538663130663235623032343436373737623130666361303866353565343862335f6f31225d2c226f7574223a5b2232393331663462386232353266303830656463366131356434353163626664363837303564633633343861663930323239343535313433343133636531373036225d2c2264656c223a5b5d2c22637265223a5b5d2c2265786563223a5b7b226f70223a2243414c4c222c2264617461223a5b7b22246a6967223a307d2c22616374222c5b2d315d5d7d5d7d11010000000000001976a9143b80a2d74a2b6dcd2f15fdea0d14aa58736de6d788ac00000000020000000147c54cad932fd6a3e00a32c8290347ba2a37a7ef4348081bbdf8c763da50d825010000006a47304402205cad6ed27d1cbc7915970f0799fed63a2ddab5952bff40d94c685e0db52d1c4f022054a4c6fa53a6284b6412293b04d34d9d88e06e61350c40a2e2a9174fd3fbefca41210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1066373264343863306637393439313033f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac00000000020000000160c98d9563a6210257bf11b1d344a0daeb5e012b4e0af4105c4a4a42208e6325010000006b48304502210087f1e6eb455a4ff1f31093947988e150275f88ddca4aef394b513eb8291aca1a0220753092cae450d05313aa5a5db298fbf4e270cd838072655e57e186efeaf87b3d41210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1030393763666337393536366531386432f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac0000000002000000012e968a6ba97feca994dc24407e6046e58c8b08107d993232adcc2204d8e7440d010000006b483045022100bf5c99c7203767701d956f78ae8b84714fd6decaa2dcda652a9335f5473dda380220029127b7d5268cad8c62d8bace68095e3d8574f49a87c47c69c4b13c031534bd41210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1039333431386335653431646433383666f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac000000000200000001e045d3990302005b1f3fcab96ad44fb19dce3f2dfe4f7879d1da02e596e04841010000006a4730440220133e56bfe7b7d393a3050c327fb2a271edd9676e94286cbcd3114e9fc1e7f0d002205b6fdf85415ff989e7e266c67b0c832b22954b5ea05c2fad6af6dda92fc32ee341210287be1a1c14c950c2045d74d40e86adbc151e2c3f528b732d58035bd2b1dfe4a7ffffffff02000000000000000013006a1064656233613035633535626333633462f4140000000000001976a9149baf3cb0ee55bf5f300b9e6fc68031e73f8985a488ac00000000"

    def "Testing BigBlockTxn deserialization"() {
        given:
            ProtocolConfig config = ProtocolConfigBuilder.get(new MainNetParams(Net.MAINNET))

            DeserializerContext context = DeserializerContext.builder()
                .protocolBasicConfig(config.getBasicConfig())
                .batchSize(2)
                .build()

            ExecutorService executor = Executors.newSingleThreadExecutor()
            BigBlockTxnDeserializer deserializer = Mockito.spy(new BigBlockTxnDeserializer(executor))

            byte[] bytes = Utils.HEX.decode(BLOCKTXN_BYTES)
            ByteArrayReader reader = ByteArrayArtificalStreamProducer.stream(bytes, byteInterval, delayMs)

            ArgumentCaptor<Message> messageCapture = new ArgumentCaptor<>()

        when:
            // Dummy Header Msg:
            HeaderMsg header = new HeaderMsg.HeaderMsgBuilder().command(PartialBlockTxnMsg.MESSAGE_TYPE).build()
            deserializer.deserializeBody(context, header, reader)
            Mockito.verify(deserializer, Mockito.times(8)).notifyDeserialization(messageCapture.capture())

            int fullSizeCount = 0
            int partialSizeCount = 0

            for (PartialBlockTxnMsg message : messageCapture.getAllValues()) {
                if (message.transactions.size() == 2) {
                    fullSizeCount++
                } else if (message.transactions.size() == 1) {
                    partialSizeCount++
                }
            }
        then:
            messageCapture.getAllValues().size() == 8
            fullSizeCount == 7
            partialSizeCount == 1

        where:
            byteInterval | delayMs
            10           | 5
    }
}

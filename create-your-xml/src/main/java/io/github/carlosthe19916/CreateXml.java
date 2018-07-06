package io.github.carlosthe19916;

import io.github.carlosthe19916.beans.*;
import io.github.carlosthe19916.sunat.TipoAfectacionIgv;
import io.github.carlosthe19916.utils.JaxbUtils;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TimeZone;

public class CreateXml {

    public static void main(String[] args) throws JAXBException, TransformerException {
        InvoiceBean invoiceBean = InvoiceBeanBuilder.InvoiceBean()
                .serie("F001")
                .numero(1)
                .codigoTipoComprobante("01")
                .observaciones("Sin observaciones")
                .fecha(
                        FechaBeanBuilder.FechaBean()
                                .fechaEmision(Calendar.getInstance().getTime())
                                .fechaVencimiento(Calendar.getInstance().getTime())
                                .build()
                )
                .moneda(
                        MonedaBeanBuilder.Moneda()
                                .codigo("PEN")
                                .tipoCambio(new BigDecimal("3.21"))
                                .build()
                )
                .impuestos(
                        ImpuestosBeanBuilder.Impuestos()
                                .igv(new BigDecimal("10"))
                                .isc(new BigDecimal("1"))
                                .build()
                )
                .total(
                        TotalBeanBuilder.Total()
                                .pagar(new BigDecimal("5"))
                                .descuentoGlobal(new BigDecimal("6"))
                                .otrosCargos(new BigDecimal("5"))
                                .build()
                )
                .totalInformacionAdicional(
                        TotalInformacionAdicionalBeanBuilder.TotalInformacionAdicionalBean()
                                .gravado(BigDecimal.ZERO)
                                .inafecto(BigDecimal.ZERO)
                                .exonerado(BigDecimal.ZERO)
                                .gratuito(BigDecimal.ZERO)
                                .build()
                )
                .proveedor(
                        ProveedorBeanBuilder.ProveedorBean()
                                .codigoTipoDocumento("6")
                                .numeroDocumento("10467793549")
                                .nombreComercial("Wolsnut4 S.A.")
                                .razonSocial("Wolsnut4 Consultores")
                                .codigoPostal("050101")
                                .direccion("Jr. ayacucho 123")
                                .region("Ayacucho")
                                .provincia("Huamanga")
                                .distrito("Jesus Nazareno")
                                .codigoPais("PE")
                                .build()
                )
                .cliente(
                        ClienteBeanBuilder.ClienteBean()
                                .codigoTipoDocumento("3")
                                .numeroDocumento("46779354")
                                .nombre("Carlos Esteban Feria Vila")
                                .email("carlosthe19916@gmail.com")
                                .direccion("Jr. carlos 997")
                                .build()
                )
                .addDetalle(
                        DetalleBeanBuilder.Detalle()
                                .cantidad(BigDecimal.ONE)
                                .precioUnitario(BigDecimal.TEN)
                                .totalIgv(new BigDecimal("1.8"))
                                .codigoTipoIgv(TipoAfectacionIgv.GRAVADO_OPERACION_ONEROSA.getCodigo())
                                .descripcion("Lapiceros de color azul")
                                .unidadMedida("NIU")
                        .build()
                )
                .build();

        InvoiceType invoiceType = BeanToType.toInvoiceType(invoiceBean, TimeZone.getDefault());

        oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory factory = new oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory();
        JAXBElement<InvoiceType> jaxbElement = factory.createInvoice(invoiceType);
        Document xmlDocument = JaxbUtils.toDocument(InvoiceType.class, jaxbElement);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(new File("output.xml"));
        Source input = new DOMSource(xmlDocument);
        transformer.transform(input, output);
    }
}

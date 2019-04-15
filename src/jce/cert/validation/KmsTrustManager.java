/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package jce.cert.validation;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.X509TrustManager;

public class KmsTrustManager implements X509TrustManager {

	private X509Certificate[] chain;
	private X509Certificate cert;
	
	public KmsTrustManager( X509Certificate cert, X509Certificate[] chain ) {
		this.chain = chain;
		this.cert = cert;
	}
	
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		try {
			verifyCertificate( cert, new HashSet<X509Certificate>(Arrays.asList( chain ) ), false );
		} catch (NoSuchAlgorithmException e) {
			throw new CertificateException( e );
		} catch (NoSuchProviderException e) {
			throw new CertificateException( e );
		} catch (InvalidAlgorithmParameterException e) {
			throw new CertificateException( e );
		} catch (CertPathBuilderException e) {
			throw new CertificateException( e );
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return chain;
	}

	private PKIXCertPathBuilderResult verifyCertificate( X509Certificate cert, Set<X509Certificate> chain, boolean verifySelfSignedCert ) throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CertPathBuilderException {

		//Check for self-signed certificate
		if ( !verifySelfSignedCert && isSelfSigned( cert ) ) {
			throw new CertificateException( " The certificate is self-signed." );
		}

		// Prepare a set of trusted root CA certificates
		// and a set of intermediate certificates
		Set<X509Certificate> trustedRootCerts = new HashSet<X509Certificate>();
		Set<X509Certificate> intermediateCerts = new HashSet<X509Certificate>();

		for ( X509Certificate oneOfChain : chain ) {
			if ( isSelfSigned( oneOfChain ) ) {
				trustedRootCerts.add( oneOfChain );
			} else { 
				intermediateCerts.add( oneOfChain );
			}
		}

		// Attempt to build the certification chain and verify it
		PKIXCertPathBuilderResult verifiedCertChain;

		verifiedCertChain = verifyCertificate( cert, trustedRootCerts, intermediateCerts, verifySelfSignedCert );

		// Check whether the certificate is revoked by the CRL
		// given in its CRL distribution point extenstion
		// LDAP을 통해 CRL을 지원할 경우 추가한다.
		//CRLVerifier.verifyCertificateCRLs(cert);

		// The chain is built and verified Return it as a result
		return verifiedCertChain;
	}

	private boolean isSelfSigned( X509Certificate cert ) throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException {

		//Try to verify certificate signature with its own public key
		try {
			PublicKey key = cert.getPublicKey();
			cert.verify( key );

			return true;
		} catch (InvalidKeyException e) {
			// Invalid key --> not self-signed
			return false;
		} catch (SignatureException e) {
			// Invalid signature --> not self-signed
			return false;
		}
	}

	private PKIXCertPathBuilderResult verifyCertificate( X509Certificate cert, Set<X509Certificate> trustedRootCerts,
			Set<X509Certificate> intermediateCerts, boolean verifySelfSignedCert ) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, CertPathBuilderException {

		//Create the selector that specifies the starting certificate
		X509CertSelector selector = new X509CertSelector();
		selector.setCertificate( cert );

		// Create the trust anchors ( set of root CA certificates ) 
		Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
		for ( X509Certificate trustedRootCert : trustedRootCerts ) {
			trustAnchors.add( new TrustAnchor( trustedRootCert, null ) );
		}

		// Configure the PKIX certificate builder algorithm parameters
		PKIXBuilderParameters pkixParams = new PKIXBuilderParameters( trustAnchors, selector );

		// Disable CRL checks( this is done manually as additional step )
		pkixParams.setRevocationEnabled( false );

		// Specify a list of intermediate certificates
		CertStore intermediateCertStore = CertStore.getInstance( "Collection", new CollectionCertStoreParameters( intermediateCerts ) );
		pkixParams.addCertStore( intermediateCertStore );

		// Build and verify the certification chain
		CertPathBuilder builder = CertPathBuilder.getInstance( "PKIX" );
		PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult)builder.build( pkixParams );

		return result;
	}
}

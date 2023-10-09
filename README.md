# Project for Cryptography and Computer Security course
Write an application that represents secure repository for storing confidential information
documents. The application should enable the storage of documents for a larger number of users in a way that access to a ceratin document is allowed only to its owner.

Users log into the system in two steps. In the first step it is necessary to enter
digital certificate that each user receives when creating an account. If the certificate
valid, the user is shown a form for entering a username and password.After successful
logging, a list of his documents is displayed through implemented interface.
The application provides the user with the option of downloading existing documents as well as
upload new documents. Each new document, before being moved to the file system, is divided into N segments (N≥4, randomly generated value), where each of these segments
moved to a different directory, in order to further increase system security and reduce
the possibility of document theft. Confidentiality and integrity must be adequately protected
of each segment, so that only the user to whom the document belongs can access his
property and see its contents. The application should detect any unauthorized modification
of stored documents and notification of the user about it, when trying to download
such documents.
The application implies the existence of a public key infrastructure. All certificates should
to be issued by the CA authority that was established before the start of the application.
Assume that the CA certificate, CRL, will be located at an arbitrary location on the file system
list, certificates of all users, as well as the private key of the currently logged-in user (not
it is necessary to implement key exchange mechanisms). It is necessary to limit the user
certificates so that they can only be used for the purposes required by the application. Around that,
the data in the certificate should be associated with the corresponding user data.
User certificates are issued for a period of 6 months. In addition, if the user in progress
If he enters the wrong credentials three times during one login, his certificate is automatically revoked
suspends and the application displays the corresponding message. After that, the application offers
the user to reactivate his certificate (if he enters the correct credentials), or register new account.

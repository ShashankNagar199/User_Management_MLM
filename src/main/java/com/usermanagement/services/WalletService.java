package com.usermanagement.services;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.usermanagement.dto.WalletCredentialDto;
//import com.usermanagement.exceptions.InsufficientBalanceException;
//import com.usermanagement.exceptions.UnableToCreateWalletAddress;
//import com.usermanagement.models.User;
//import com.usermanagement.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.web3j.abi.FunctionEncoder;
//import org.web3j.abi.TypeReference;
//import org.web3j.abi.datatypes.Address;
//import org.web3j.abi.datatypes.Function;
//import org.web3j.abi.datatypes.Type;
//import org.web3j.abi.datatypes.generated.Uint256;
//import org.web3j.crypto.*;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.core.methods.response.EthSendTransaction;
//import org.web3j.protocol.http.HttpService;
//import org.web3j.utils.Convert;
//import org.web3j.utils.Numeric;
//
//import java.io.File;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.List;

//@Service
public class WalletService {

//    @Value("${ethereum.node-url}")
//    private String nodeUrl;
//
//    @Value("${ethereum.wallet.file}")
//    private String walletFile;
//
//    @Value("${ethereum.wallet.password}")
//    private String walletPassword;
//
//    @Value("${tron.node-url}")
//    private String tronUrl;
//
//    private final Web3j web3j;
//
//    @Autowired
//    UserRepository userRepository;
//
//    public WalletService() {
//        this.web3j = Web3j.build(new HttpService(tronUrl));
//    }
//
//    public Credentials loadWalletCredentials() throws Exception {
//        // Load wallet
//        File walletFile = new File(this.walletFile);
//        return WalletUtils.loadCredentials(walletPassword, walletFile);
//    }
//
//    public BigInteger getBalance(String address) throws Exception {
//        return web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
//                .send()
//                .getBalance();
//    }
//
//    public WalletCredentialDto createTronAccount() {
//        try {
//            Credentials credentials = createNewWallet();
//            WalletCredentialDto walletCredentialDto = new WalletCredentialDto();
//            walletCredentialDto.setWalletAddress(credentials.getAddress());
//            walletCredentialDto.setPrivateKey(credentials.getEcKeyPair().getPrivateKey().toString(16));
//
//            return walletCredentialDto;
//        } catch (Exception e) {
//            // Handle exception
//            throw new UnableToCreateWalletAddress("Unable to create wallet address");
//        }
//    }
//
//    private Credentials createNewWallet() throws Exception {
//        // Specify the directory where the wallet file should be stored
//        String walletDirectory = "C:\\Users\\hp\\OneDrive\\Desktop";
//
//        // Create a new wallet file and return the credentials
//        return WalletUtils.loadCredentials("wallet321", walletDirectory+"\\wallets");
//    }
//
//    public String generateNewWalletAddress() throws Exception {
//        // Generate new Ethereum account
//        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
//        WalletFile walletFile = Wallet.createLight(walletPassword, ecKeyPair);
//
//        // Save the wallet file (you may need to adjust this based on your storage mechanism)
//        String walletFileName = "user_wallet_" + System.currentTimeMillis() + ".json";
//        String walletFilePath = "/path/to/store/wallets/" + walletFileName;
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.writeValue(new File(walletFilePath), walletFile);
//
//        return Keys.toChecksumAddress(walletFile.getAddress());
//    }
//    public String sendEther(String fromAddress, String toAddress, BigDecimal amount) throws Exception {
//        Credentials credentials = loadWalletCredentials();
//
//        // Convert amount to wei
//        BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
//
//        // Create transaction
//        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
//                BigInteger.ZERO,
//                BigInteger.valueOf(21_000),
//                BigInteger.valueOf(21000),
//                toAddress,
//                value
//        );
//        // Sign and send transaction
//        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//        String hexValue = Numeric.toHexString(signedMessage);
//
//        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
//        return ethSendTransaction.getTransactionHash();
//    }
//    public String sendUSDT(String fromAddress, String toAddress, BigDecimal amount) throws Exception {
//        Credentials credentials = loadWalletCredentials();
//
//        // USDT contract address on Tron (replace with the actual USDT contract address)
//        String usdtContractAddress = "0x123456789abcdef0123456789abcdef012345678";
//
//        // Create USDT transaction
//        Function function = new Function(
//                "transfer",
//                List.of(new Address(toAddress), new Uint256(amount.multiply(BigDecimal.valueOf(1e6)).toBigInteger())),
//                List.of(new TypeReference<Type>() {})
//        );
//
//        String encodedFunction = FunctionEncoder.encode(function);
//
//        RawTransaction rawTransaction = RawTransaction.createTransaction(
//                BigInteger.ZERO,
//                BigInteger.valueOf(21_000),
//                BigInteger.valueOf(21000),
//                usdtContractAddress,
//                encodedFunction
//        );
//
//        // Sign and send USDT transaction
//        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//        String hexValue = Numeric.toHexString(signedMessage);
//
//        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
//        return ethSendTransaction.getTransactionHash();
//    }
//
//
//    public void depositAmount(String walletAddress, Double amount) throws Exception {
//        User user = userRepository.findByWalletAddress(walletAddress);
//        if (user != null) {
//            Double currentBalance = user.getBalance();
//            Double newBalance = currentBalance+amount;
//            user.setBalance(newBalance);
//            userRepository.save(user);
//        }
//    }
//
//    public void withdrawAmount(String walletAddress, Double amount) throws Exception {
//        User user = userRepository.findByWalletAddress(walletAddress);
//        if (user != null) {
//            Double currentBalance = user.getBalance();
//            if (currentBalance.compareTo(amount) >= 0) {
//                Double newBalance = currentBalance - amount;
//                user.setBalance(newBalance);
//                userRepository.save(user);
//            } else {
//                throw new InsufficientBalanceException("Insufficient balance for withdrawal");
//            }
//        }
//    }
}

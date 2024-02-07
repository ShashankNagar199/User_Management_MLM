package com.usermanagement.services;

import com.usermanagement.dto.ReferralTreeResponse;
import com.usermanagement.dto.TotalSalesByChildren;
import com.usermanagement.models.SalesRecord;
import com.usermanagement.models.User;
import com.usermanagement.models.UserRank;
import com.usermanagement.repositories.SalesRecordRepository;
import com.usermanagement.repositories.UserRankRepository;
import com.usermanagement.repositories.UserRepository;
import com.usermanagement.util.CustomUtils;
//import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.usermanagement.exceptions.*;
import org.springframework.scheduling.annotation.Scheduled;

//import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public class ProductSalesService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRankRepository userRankRepository;

    @Autowired
    private SalesRecordRepository salesRecordRepository;
    @Autowired
    private CustomUtils customUtils;

    @Transactional
    public SalesRecord addProductSales(String product,String productAmount,String userId){
        User user= userRepository.findByUserId(userId);
        Double availableBalance=user.getBalance();
        Double amount=customUtils.convertStringToDouble(productAmount);
        if(availableBalance<amount){
            throw new InsufficientBalanceException("Insufficient balance for purchasing");
        }
        Double currentBalance=availableBalance-amount;
        Double sales=user.getSales();
        user.setBalance(currentBalance);
        Double currentSales=sales+amount;
        user.setSales(currentSales);
        TotalSalesByChildren salesOfSubNodes= userRepository.getSalesOfChildrenNodesByUserId(user.getUserId());
        Double subSales=0.0;
        if(salesOfSubNodes==null){
            subSales=0.0;
        }
        else{
            subSales=salesOfSubNodes.getSales();
        }
        Long rankId=calculateUserRank(currentSales,subSales);
        UserRank userRank=userRankRepository.findUserRankById(rankId);
        user.setUserRank(userRank);
        userRepository.save(user);
        if(user.getReferrer()!=null){
            updateRewardAmountToParentNodes(user.getUserId(),user.getLevel()+1,productAmount);
        }

        SalesRecord salesRecord = new SalesRecord(user, product);
        return salesRecordRepository.save(salesRecord);
    }

    int interestPerProduct(String product){
        int interest = switch (product) {
            case "level1" -> 1;
            case "level2" -> 1;
            case "level3" -> 1;
            case "level4" -> 2;
            case "level5" -> 2;
            default -> 0;
        };
        return interest;
    }

    public void updateRewardAmountToParentNodes(String userId,Integer level,String ProductAmount){
        List<ReferralTreeResponse> listOfParentNodesWithLevels = userRepository.findReferralNodeTree(userId,level);
        for (ReferralTreeResponse referralTreeResponse : listOfParentNodesWithLevels) {
        User user= userRepository.findByUserId(referralTreeResponse.getUserId());
        int rewardPercentage=calculateReward(referralTreeResponse.getLevel(),user.getUserRank().getId());
        Double userSales=user.getSales()+Double.parseDouble(ProductAmount) *rewardPercentage/100;
        user.setSales(userSales);
        userRepository.save(user);
        }
    }

    public void updateUserRank(String userId){
        User user=userRepository.findByUserId(userId);
        TotalSalesByChildren salesOfSubNodes= userRepository.getSalesOfChildrenNodesByUserId(user.getUserId());
        Double totalIndividualSales=user.getSales();
        Long rankId=calculateUserRank(totalIndividualSales,salesOfSubNodes.getSales());
        user.getUserRank().setId(rankId);
        userRepository.save(user);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run every day at midnight
    public void calculateInterest() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<SalesRecord> salesRecords = user.getSalesRecords();
            for (SalesRecord salesRecord : salesRecords) {
                int interestRate = interestPerProduct(salesRecord.getProduct());
                int interest = calculateInterestForRecord(salesRecord, interestRate);
                salesRecord.setInterest(interest);
                salesRecordRepository.save(salesRecord);
            }
        }
    }

    private int calculateInterestForRecord(SalesRecord salesRecord, int interestRate) {
        LocalDateTime timestamp = salesRecord.getTimestamp();
        LocalDateTime now = LocalDateTime.now();
        long hoursBetween = java.time.Duration.between(timestamp, now).toHours();
        return (int) (hoursBetween / 24) * interestRate;
    }

    private Long calculateUserRank(Double Sales,Double subSales){
        Long rank= 1L;
        if(Sales>=1000 && Sales<2000){
            rank=1L;
        }
        else if(Sales>=2000 && Sales<5000 || subSales>=5000 && subSales<10000){
            rank=2L;
        }
        else if(Sales>=5000 && Sales<10000 || subSales>=10000 && subSales<50000){
            rank=3L;
        }
        else if(Sales>=10000 && Sales<50000 || subSales>=50000 && subSales<1000000){
            rank=4L;
        }
        else if(Sales>=1000000){
            rank=5L;
        }
        return rank;
    }

    private int calculateReward (Integer level, Long star){
        // calculate the commission for the current level
        int commission = 0;
        if (level == 1) {
            if (star == 0) {
                commission = 10;
            } else if (star == 1) {
                commission = 10;
            } else if (star == 2) {
                commission = 8;
            } else if (star == 3) {
                commission = 8;
            } else if (star == 4) {
                commission = 8;
            }
        } else if (level == 2) {
            if (star == 0) {
                commission = 5;
            } else if (star == 1) {
                commission = 5;
            } else if (star == 2) {
                commission = 5;
            } else if (star == 3) {
                commission = 5;
            } else if (star == 4) {
                commission = 5;
            }
        } else if (level == 3) {
            if (star == 1) {
                commission = 5;
            } else if (star == 2) {
                commission = 5;
            } else if (star == 3) {
                commission = 5;
            } else if (star == 4) {
                commission = 5;
            }
        }
        else if (level == 4) {
            if (star == 1) {
                commission = 5;
            } else if (star == 2) {
                commission = 5;
            } else if (star == 3) {
                commission = 5;
            } else if (star == 4) {
                commission = 5;
            }
        }
        else if (level == 5) {
            if (star == 2) {
                commission = 3;
            } else if (star == 3) {
                commission = 3;
            } else if (star == 4) {
                commission = 3;
            }
        }
        else if (level == 6) {
            if (star == 2) {
                commission = 3;
            } else if (star == 3) {
                commission = 3;
            } else if (star == 4) {
                commission = 3;
            }
        }
        else if (level == 7) {
            if (star == 2) {
                commission = 3;
            } else if (star == 3) {
                commission = 3;
            } else if (star == 4) {
                commission = 3;
            }
        }
        else if (level == 8) {
            if (star == 3) {
                commission = 2;
            } else if (star == 4) {
                commission = 2;
            }
        }
        else if (level == 9) {
            if (star == 3) {
                commission = 2;
            } else if (star == 4) {
                commission = 2;
            }
        }
        else if (level == 10) {
            if (star == 3) {
                commission = 2;
            } else if (star == 4) {
                commission = 2;
            }
        }
        else if (level == 11) {
            if (star == 4) {
                commission = 1;
            }
        }
        else if (level == 12) {
            if (star == 4) {
                commission = 1;
            }
        }
        else if (level == 13) {
            if (star == 4) {
                commission = 1;
            }
        }
        else if (level == 14) {
            if (star == 4) {
                commission = 1;
            }
        }

        return commission;
    }
}

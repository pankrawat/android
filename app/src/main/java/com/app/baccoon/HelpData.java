package com.app.baccoon;

/**
 * Created by admin1 on 10/8/16.
 */
public class HelpData {


   public enum Topics{
        gettingStarted, sellingOnBaccoon, buyingOnBaccoon, tailorYourExp, baccoonPayment, aboutDetails

    }

    public String[] getQuestions(int index) {
        switch (index) {
            case 0:
                return gettingStarted;
            case 1:
                return sellingOnBaccoon;
            case 2:
                return buyingOnBaccoon;
            case 3:
                return baccoonPayment;
            case 4:
                return tailorYourExp;
            case 5:
                return aboutDetails;

        }
        return null;
    }


 public String[] getAnswers(int index)
    {
        switch(index) {
            case 0:
                return gettingStartedAnswerContent;
            case 1:
                return sellingOnBaccoonAnswer;
            case 2:
                return buyingOnBaccoonAnswer;
            case 3:
                return baccoonPaymentAnswer;
            case 4:
                return tailorYourExpAnswer;
            case 5:
                return aboutDetailsAnswer;

        }

return null;
    }



    String[] gettingStarted={
    "What can I do on Baccoon?",
    "I have problems logging in Baccoon?",
    "How can I recover my password on Baccoon?",
    "Why can I register with Facebook,Vkontakt OR E-mail?",
    "Can I change my profile information after registration?",
    "Promotional codes and selling on Baccon?"};


    String[]  sellingOnBaccoon={
    "How does selling on Baccoon work?",
    "What should be sold in the app?",
    "How do I set the right price?",
    "What if I want to receive bids",
    "When is the sale legally binding?",
    "When can I cancel a deal?",
    "Can I get in touch with a buyer after I cancelled a deal?",
    "How can I edit my ads?",
    "Which items are prohibited?",
    "Which items we like and favor?",
    "Be aware of the picture size?",
    "With a heavy heart I’m giving up my pet. How do I do it right?",
    "Secure payment even when shipping?",
    "How do I send items secure and in a correct way on Baccoon?",
    "How do I become a Young Designer on Baccoon?",
    "How do I become a young fashion oriented item seller?",
    "How can I join to Membership accounts?"};

    String[]  buyingOnBaccoon={
    "How does buying on Baccoon work?",
    "When have I legally agreed to buy an item?",
    "When must or should I cancel a deal?",
    "Can I send a new offer to a seller after cancelling a deal?",
    "Why doesn't my last deal show up in my sales and buys?",
    "How much time do I have to expect for delivery?",
    "Can I send a private message?",
    "What should I consider before buying a pet?",
    "xHow does notification works?"};

    String[]  tailorYourExp={
    "What are newsletters and search alerts?",
    "What happens if I follow a user?",
    "What if something unexpected happens?",
    "Is Baccoon also available on my PC?",
    "How can I delete my account?"};

    String[]  baccoonPayment={
    "Membership area",
    "Picture wise",
    "Custom upload"};

    String[]   aboutDetails={
    "What does the term Baccoon mean?",
    "What's the App's purpose?",
    "How did you come up with the idea?",
    "Free classifieds: is this really true?",
    "How can I support Baccoon?"};



    String[]   gettingStartedAnswerContent={
    "Baccoon works like your favorite boot sale – just easier. You can create your offer quickly by taking a picture, writing a title, a short description and setting a price. Now anybody using the app in your local area can see your offer. To speed up sales, you can share your offer via Facebook, vkontakte, hike,skype, google and messaging or E-mail with your friends, business friends or loved ones. Did you find an object of desire sold by another user? Great, because with Baccoon, the smartphone app, it's quite simple to negotiate the price and agree on where and when it comes to the golden handshake.",
    "Login with E-mail or Facebook\n\n Make sure that you are using the same type of login as you registered with, this could be with Facebook or with your E-mail and password. If you registered with Facebook, go to the login and click on “Log in with Facebook”. If you registered with your E-mail, click on the “Log in with E-mail + SMS”and enter your E-mail and the password you chose when registering. If you have forgotten your password, click on “forgot your password?” and follow the guidelines. NB! When you registered with Facebook, then you cannot get a new password from Baccoon and you cannot access Baccoon if you delete or deactivate your Facebook account.\n If you're still not able to login following these steps, please get in touch with our support team.",
    "Work under construction",
    "Baccoon Marketplace is safe and has fraud protection security software \n Two simple taps on the screen and you are registered via Facebook. It's fast and simple. \n Registration with E-Mail + email confirmation: To keep Baccoon as secure as we can, we require that users registered via E-Mail also confirms her/his identity via a email confirmation. \n We hope that by employing these registration methods, we can also stop annoying spammers and scammers – you've surely experienced the problem with other marketplaces!",
    "Oops – I have provided wrong data while registration \n\n You may change your E-mail and your profile picture in your personal profile. Your username and telephone number cannot be changed.",
    "Work under construction"};

    String[]   sellingOnBaccoonAnswer={
    "You can sell items on our mobile market place and give a \nDo you have things that you don't need any more or want to earn some extra cash? Go to sell: upload pictures of your actual item, write a title and a short description. Set a fair price – and you're almost ready to go!\nYou need to either upload with a voucher some money to pay pictures or by joining our member ship area.",
    "Everything that has made you happy sometime ago. Whether it's concert tickets that you cannot use yourself, a washing machine, an old dresser, a lovingly restored bike, a fancy cupboard, your former favorite blouse, your used car, rare sunglasses or your favorite game that is waiting for a new user. Baccoon is the marketplace for everything loved by you.",
    "Ask yourself the following question: What is the highest price I would pay for the product? The condition of the item and any flaws should be considered when setting the price. We especially appreciate: realistic and appropriate prices. Honesty plays a very important role for us.",
    "Stating a 0,- sales price on your offered product gives the impression that you want to give it away for free. Therefore we request you to set an asking price, i.e. the price you would like to sell the product for. Therewith we want to prevent misunderstandings and increase your likelihood of selling your product.",
    "A legally binding agreement is entered when both sides agree on the deal, this means that one party accepts & the other confirms. Offers are not legally binding. \nIf you accept an offer from a buyer, the buyer will have to confirm the deal before it is binding. If you send a counter offer to the buyer and he/she accepts, you will have to confirm the deal before it is binding.",
    "You can cancel a deal until the deal has been confirmed by you or the interested buyer. After a deal has been confirmed you can no longer cancel the deal, and you have entered into a legally binding agreement to sell your product.","After a deal was cancelled you can no longer contact the buyer, but you can receive new offers on your item from the same and other users. Make a counter offer if you have other pending offers to sell your product faster.",
    "Go to your product under the heading “Selling” in your profile, click “Edit” to change information about your ad such as title, description, price and category. After getting an offer on your ad, you may no longer change your price in the ad, but you can make a counter offer to the interested buyer. The location of the product cannot be changed after inserting your ad, so make sure to submit your ad at a place where your product can be picked up.",
    "Illegal items are prohibited even at Baccoon. Generally, it is not allowed to sell living creatures or food. Moreover, you are not allowed to sell things, that might physically or emotionally damage someone, or are regulated by law, i.e. that are not allowed to be sold to people without a certain license. This is also true for fake/counterfeit products.\n\nHere are some examples of things that are not allowed: \nPhotos that do not show the item or the item is not clearly visible.Internet photos i.e. you have not taken the picture yourself. \nItem infringes trademarks or copyrights of others (e.g. counterfeit product like fake bags etc.). \nItem is perishable (food etc.). \n You are selling a pet outside India,Russia,Brasil or the UK. \n Advertisements \nPhoto or text containing a reference to another website, app or shop. \n Items that are defective or of poor quality. \n Breach of good manners, the law or legal restrictions. \n  Item listed as free but is not intended to be given away. \nThere is no realistic price for the item (asking for counteroffers). \n More information can be found in the General Terms and Conditions §6. \n\nFor services the following content is forbidden: \n\nPhotos or text containing a reference to another website, app or shop.\nOffers containing lotteries or roulette systems. \nUntrustworthy homework-services / photoshoots, multilevel-marketing, referral marketing \nJob offers or requests for persons under the age of 14. \nOffers containing phone numbers with additional charges \nOffers selling social network fan pages/likes or followers \nOffers of with sexual services or products \nPhotos or titles and descriptions giving a false impression of the content of the service \nServices without an appropriate price or listed with the price of 0,-. \nServices containing an internet or catalogue photo.",
    "Work under construction",
    "Work under construction",
    "Although selling a living creature is forbidden, separate rules are applicable for offering and selling pets on Baccoon in the UK and Germany (the law of the specific country will take place). In these countries the offering and selling of pets are allowed but must comply with the following rules: \n\nThe description or the picture of the pet should be chosen wisely and with respect. The buyer needs to be informed about the age, diseases or any kind of abnormal characteristics of the pet \n\nSelling pets to users younger than 18 years. \nStreet sales or offering, requesting or confirming postal deliveries of pets. \nSelling protected species and compounds without the necessary documents. For more information have a look at the protected species and sites.\nSelling exotic, dangerous and poisonous animals.\nSelling pets with any controversial surgeries which are not mandatory for the pet’s health (ie. ear-cropping, tail docking, devocalization, claws or teeth removal). Requesting such surgeries is strictly prohibited. \nSelling pets which are located in another country or selling pets as live food.\nProfessional breeding without any approval from an administrative authority. \nSelling of multiple different species or selling pets in huge quantities, as this conflicts with the proper treatment of animals.\nSelling pets before they reach an appropriate age. The minimum age for puppies and kittens is 8 weeks or older. The mother animal is on site when you pick up it up.\nUsers selling pets are obliged to get informed about the legal regulations and are forced to comply with them. If you notice a pet ad that does not comply with the rules, please report it via the “report” function on the respective ad page. Further information regarding animal welfare can be found in each country’s specific regulations: United Kingdom, Germany, and for the EU \n\nWhen selling your pet please also take into account the following highly important information: \n\nPets are not products, fashion accessories or toys! \nWhen selling your pet you must act responsibly and treat your pet with respect. \nPlease fully inform the buyer about any diseases, health problems, special requirements or behavioral traits your pet may have. \nYour main intention must be to find a loving and caring new home for your pet – not to make a profit! \nIt is advisable to set up a short interview with any prospective new owners, to make sure your pet is going to a loving home with the right person. \nWe hope you will find a loving new home for your pet soon!",
    "If you do not meet in person to hand over the product, but arrange to ship it, here are some tips regarding payment and delivery:\n\nbut by buyer: the buyer can choose how he wants to get the item (shipping terms apply)shipping by post, dhl or pick up\n\nPay on delivery: With this payment method, e.g. available at UPS, the goods will only be handed over to the buyer when paying the agreed price to the delivery man. \nPaypal: The whole payment process will be performed via your Paypal account. The money will be debited from the buyers credit card/bank account and will be accredited to the seller.Then the seller will ship the item to you. Clear advantages: The seller benefits from fast payment. The buyer enjoys special protection in case he should not receive the item from the seller. For more information on the Paypal buyer protection click here. \nImportant note: Every PayPal account has to be assigned to a unique and active E-mail account. Are you planning to handle your next sale/purchase via PayPal? Please make sure the PayPal account belongs to the user you are talking to. Please be cautious when the partner you are trading with insists on using the PayPal of a third party.",
    "Offering shipping is not mandatory, do this if you like to ship your items and if you have enough time. To prevent misunderstandings and long explanations we recommend stating this in your ad, and specify how you offer shipping and who should pay for it. \n\nIf a buyer makes an offer “including shipping” he offers to pay for the shipping costs – however,he already includes them in the price he offered to pay for the item. \n\nEven if it sometimes means an extra effort, it can be quite beneficial to ship the item insured or at least traceable because a lost parcel is annoying for both parties. Therefore it is also very important to keep the shipping receipt or the receipt from the post office until the deal is finally concluded.",
    "On Baccoon we love to support young stylish fashion brands and talented young designers in our special “Fashion or Accessoiries” category. This category is a beautiful space of innovative designs and creative products, and we couldn't be happier about it! If you are a YoungDesigner and would like to join in, please write a short email telling a little something about your work to Amit at ascbaccoon.com. \n\nNeedless to say we do ask you meet certain criteria when submitting work to the “YoungDesigner” category. Images of your product(s) should be shot in front of a plain background, be very well lit and with the focus being on the products itself. \n\nAll submissions will be taken into consideration by the Baccoon team. If you are successful in becoming a Baccoon Fashion Designer, your profile picture will receive an orange badge with the title “Fashion and Accessoiries”. You have the chance to add your own items and logistics options under MyBaccoon – Settings.",
    "Work under construction",
    "Work under construction",
    "Work under construction"};

    String[]   buyingOnBaccoonAnswer={
    "If you know exactly what you want to buy you can use our search function. If you are looking for inspiration we have special selections, new items in your area and categories you can find in our menu. When you find something you want to buy, then click on “Make an offer” when you are on the item's page, write the price you think is fair to pay with an optional personal message to the seller. Wait for the seller to respond to your offer and finalize the deal.",
    "A legally binding agreement is entered when both sides agree on the deal, this means that one party accepts & the other confirms. Offers are not legally binding. \n\nIf the seller accepts your offer, you will have to confirm the deal before it is binding. You can respond to the seller's counter offer with another counter offer or accept his offer, then he/she will have to confirm the deal before it is binding.",
    "You can cancel a deal until the deal has been confirmed by you or the seller. After a deal has been confirmed you can no longer cancel the deal, and you have entered into a legally binding agreement to buy the product. \n\nWhen you have bought the product you want, and have several offers on similar products that you no longer want, you must cancel these deals. If you have already accepted deals on such products, it is important that you cancel these before the seller confirms if you no longer want the product. If not, you have legally bought the product when the seller confirms. \n\nYou can cancel deals in the dialogue with the seller by using the “cancel” button if the deal is waiting confirmation, or in the options button (three dots) in the upper right corner. You can also cancel by clicking the “x” on the product picture in your watchlist.",
    "You can send a new offer to the seller after cancelling the deal, this will open as a completely new offer and not in the same dialogue as your cancelled offer. Write a nice message to the seller, and your chances that he/she will accept your offer are higher.",
    "Using the “Question” function you may get in touch with other users, exchange information about the product and even agree on a meeting point. In order to “officially” close the deal, use the “Make an offer” function to make and accept an offer - only then it will show up in your profile as a new sale or that you have bought something.",
    "According to our experience the average shipping time is between 5 and 10 days. Since all deals are private sales it's unrealistic to expect shipping times like Amazon offers.",
    "To communicate with a seller you may either ask questions about the item open for everyone to see or use the “Make an offer” function. If you want to ask something private before making your offer, make a 0 offer stating that this is only to exchange private information and have a private conversation with the seller. You may continue that chat after an offer is accepted to agree on a pickup point and other practicalities.",
    "Please take this important information into account before buying a pet: \n\nYou are not allowed to buy a pet on Baccoon if you are younger than 18 years. \nPets are not products, fashion accessories or toys – but living creatures just like us! \nPets are social beings which belong to your family. When buying a pet you should bear it in mind that they need care, love and time – like you and me! \nBefore buying a pet please make sure that you can offer it a home for life. If you go on holiday or get sick you will need to provide it with alternative accommodation. \nPlease note that purchasing an animal means a bigger investment than just the initial cost. Please take the time to consider vet bills, food and other living costs. \nYou should not buy a pet out of pity. \nA pet is not to be delivered by mail or in any other way which is likely to harm, distress or endanger the animal. \nYou do not demand or support any controversial pet surgeries (e.g. ear-cropping, tail docking, devocalization, claws or teeth removal). \nYou can identify a reliable seller or registered breeder by the following criteria: \nPets are sold at an appropriate age. Minimum age for puppies is 8 weeks and for kittens 12 weeks. \nA professional breeder is able to show you proof of his breeding site. \nPets are handed over to their new owners at the seller’s residence. Street sales and postal deliveries are NOT acceptable. \nThe mother animal is on site when you pick up a puppy or kitten. \nThe seller offers only one breed. A wide variety of available breeds indicates a questionable seller. The seller ensures the well-being of the animals and can show his/her license and provide the medical records of any animal on their site.\nFor further information please refer to the country-specific animal welfare regulations for United Kingdom, Germany and the EU. (country law applies). ",
    "Work under construction"};

    String[]  tailorYourExpAnswer= {
            "Our newsletter will keep you up to date on exiting information and news about the App. You may activate or deactivate the newsletter in your setting in your profile under “My Baccoon”. \n\nAre you looking for a certain product and would like to get notified as soon as someone sells this in your neighbourhood? Our search alert is helps you keep up to date! Just enter the search term you are interested in following in the side menu, and as soon as we have new hits, you'll be notified in the app and by E-mail. If you wish to remove a search alert, click on the grey “X” on the right side of your search alert in the side menu.",
            "If you look at an item (in a dialog or a product) you have the option to “favourite“ by clicking on the star to stay informed and be the first to know if this offering is still valid or something new happens. In the category “favourites” you will always be kept up to date. The items your Facebook friends and the users you choose to follow are offered as well. \n\nYou may “unfollow” a user the same way you followed him/her. Go the the person's profile and deactivate by clicking on the yellow star.",
            "If a Baccoon user does not get back to you or any other problems should occur, please contact the Baccoon support team. Our support team will assist and advise you via supportbaccoon.com or (check international help line +1800 121 2400 2100). \n\nDo you have more questions? Send us an email to supportbaccoon.com or use our feedback form. \nOur baccoon feedback form, you will find here. ",
            "Baccoon is available as a classifieds app for iOS devices (iPhone, iPad, iPod) in the Apple App Store as well as for Android devices (smartphones, tablets) in the Google Play Store. As long as you have a device with one of the two operating systems, you can easily buy and sell the most beautiful second-hand goods. The mobile boot sale is always open, no matter if you'reon the move or at home and at any time of the day.",
            "We would love to keep you as a user! If you really want to leave us, please send us an E-mail with your user information (E-mail and username) at supportBaccoon.com. Please provide uswith a short explanation telling us why you would like to delete your account - your feedback helps us improve our app!",
    };

    String[]   baccoonPaymentAnswer={

    "You have the choice of different memberships, you can also choose plan period of 3 months, 6 months or 12 months. with plan type small, medium or large picture uploads quantities. Memberships ends by months, please renew your membership when it is finished. We have given great discounts  for picture upload by taking membership accounts.",
    "you can also upload picture by picture paying picturewise, the app calculates the amount of pictures you like to upload. You just have to upload via paypal, credit card or cash. When the amount of pictures are used you can always upload either with paypal, credit card or cash again your new amounts of picture to be used. ",
    "You can upload the amount of your payment in calculates the amount of picture you can get with it.",
};


    String[]   aboutDetailsAnswer={
        "The baccoon is a flying mammal about the size of a full grown raccoon. We empashize here of the possiblity to rummage or forage in goods being used and to resell them. Raccoons/Baccoon are smart animals. We encourage to reuse things and not waste them. As a smartphone app, Baccoon – the boot sale app for beautiful things, is the shop you always carry in your pocket.",
    "Start the iPhone, iPad or Android app and you can instantly buy or sell used and extraordinary items. If you're lucky, you might even find a offer in your area while browsing through the items in your neighbourhood. With mobile range of 10-100 km/miles you can limit your search range. You can say I want to see articles in the range of 50 or 100 kilometers/miles of my living place.\nSecond hand, DIY, used or simply beautiful. If you're not looking to buy or sell something in particular, just rummage through the pictures and get inspired. Be it from the charm of an old photo camera, a rare piece of furniture or a fancy piece of clothing.\n“Smartphone enables fast,easy and mobile sale”",
    "People love to negotiate, whether it is on a fleamarket or arabian bazar, we love trading and selling of products on markets. We came to the idea that actually so many goods or stuffs lie around we'd like to sell instead. Old stuff which we loved but not using anymore. can be given away. In todays digital world there are no frontiers to sell anymore but we still walk into fleemarkets and love to see goods, sometimes we need to sell it fast because several constraints apply of not being able to go to a fleamarket. More and more power sellers, spammers and people who have no good intentions, contributed to the fact, that the initial charm of these portals was lost.\n\n What began as a small idea has quickly drawn us into the spell, and we all invested time, passion, and creativity to build something great. One thing was clear, if Baccoon is the boot sale app for beautiful things, Baccoon needs to be beautiful too. Both as an Android App and iOS App.",
    "Save the best for last: Your options for negotiating, swapping, buying, selling and bartering in this particular market are endless.\nFashion and accessoires designers wining awards\nwe encourage you to become one of our most visible and favourite seller on our app. If your item is outstanding and users love your items we will contact you and tell it to you.\nWe also perform campaigns and award wining prices, once a year we elect our most outstanding seller he will win a price of being the most loved seller mentioned by users on our platform.\nYou can share you product and product name via social media and whats app as well. ",
    "Share your experience with baccoon products with your friends and family and like us on Facebook, hike, vkontakte,whats app, Twitter and Google+. If you even have your own blog or homepage we would appreciate it if you write about your positive experience with Baccoon. We're also happy to send you more information or images."};





}

'use strict';
module.exports = function(sequelize, DataTypes) {
  var workers = sequelize.define('workers', {
    username: DataTypes.STRING,
    password: DataTypes.STRING,
    wxid: DataTypes.STRING,
    geo: DataTypes.GEOMETRY('POINT'),
    geo2: DataTypes.GEOMETRY('POINT'),
    name: DataTypes.STRING,
    age: DataTypes.INTEGER,
    sex: DataTypes.INTEGER,
    photourl: DataTypes.STRING,
    from: DataTypes.STRING,
    expyears: DataTypes.STRING,
    content: DataTypes.STRING,
    rate_pro: DataTypes.FLOAT,
    rate_communicate: DataTypes.FLOAT,
    rate_skill: DataTypes.FLOAT,
    tel: DataTypes.STRING,
    address: DataTypes.STRING,
    hospital: DataTypes.STRING,
    keshi: DataTypes.STRING,
    position: DataTypes.STRING,
    normal_cert: DataTypes.STRING,
    expert_cert: DataTypes.STRING,
    total_prize: DataTypes.FLOAT,
    is_recommend: DataTypes.INTEGER,
    review_status: DataTypes.INTEGER,
    id_number: DataTypes.STRING,
    frozen_prize:DataTypes.INTEGER,
    address2: DataTypes.STRING,
    geoaddress: DataTypes.STRING,
    geoaddress2: DataTypes.STRING,
    city: DataTypes.STRING,
    goodat:DataTypes.STRING,
    created_time:DataTypes.INTEGER,
    account_status:DataTypes.INTEGER
  }, {
       timestamps: false
  },{
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return workers;
};
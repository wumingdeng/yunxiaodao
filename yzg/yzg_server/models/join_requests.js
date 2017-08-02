'use strict';
module.exports = function(sequelize, DataTypes) {
  var join_requests = sequelize.define('join_requests', {
    userid: DataTypes.STRING,
    realName: DataTypes.STRING,
    nickName: DataTypes.STRING,
    review: DataTypes.INTEGER,
    headUrl: DataTypes.STRING,
    phone: DataTypes.STRING,
    job: DataTypes.INTEGER,
    hospital: DataTypes.STRING,
    department:DataTypes.STRING,
    upid: DataTypes.STRING,
    upName: DataTypes.STRING,
    requestDate: DataTypes.INTEGER,
    certificate: DataTypes.STRING,
    status: DataTypes.INTEGER,
    advice: DataTypes.STRING,
    approver: DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return join_requests;
};
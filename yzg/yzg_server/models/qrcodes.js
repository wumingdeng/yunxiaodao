'use strict';
module.exports = function(sequelize, DataTypes) {
  var qrcodes = sequelize.define('qrcodes', {
    userid: DataTypes.STRING,
    ticket: DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return qrcodes;
};
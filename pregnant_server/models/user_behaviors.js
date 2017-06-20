'use strict';
module.exports = function(sequelize, DataTypes) {
  var user_behaviors = sequelize.define('user_behaviors', {
    userid: DataTypes.STRING,
    time: DataTypes.INTEGER,
    api: DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return user_behaviors;
};
package com.weareblox.assessment.coin.api.command;

import java.util.Objects;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class AddCoinCommand {

	@TargetAggregateIdentifier
	private String coinId;

	private int quantity;

	private String orderId;

	private boolean isRevert;

	public AddCoinCommand() {

	}

	public AddCoinCommand(String coinId, int quantity) {
		super();
		this.coinId = coinId;
		this.quantity = quantity;
	}

	public AddCoinCommand(String coinId, int quantity, String orderId) {
		super();
		this.coinId = coinId;
		this.quantity = quantity;
		this.orderId = orderId;
	}
	
	public AddCoinCommand(String coinId, int quantity, String orderId, boolean isRevert) {
		super();
		this.coinId = coinId;
		this.quantity = quantity;
		this.orderId = orderId;
		this.isRevert = isRevert;
	}

	/**
	 * @return the coinId
	 */
	public String getCoinId() {
		return coinId;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coinId, orderId, quantity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AddCoinCommand other = (AddCoinCommand) obj;
		return Objects.equals(coinId, other.coinId) && Objects.equals(orderId, other.orderId)
				&& quantity == other.quantity;
	}

	/**
	 * @return the isRevert
	 */
	public boolean isRevert() {
		return isRevert;
	}

	/**
	 * @param isRevert the isRevert to set
	 */
	public void setRevert(boolean isRevert) {
		this.isRevert = isRevert;
	}
}
